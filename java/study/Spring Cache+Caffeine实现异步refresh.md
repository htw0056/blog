# Spring Cache+Caffeine实现异步refresh

> 本文目标：用尽量简单优美的方式解决[Multiple Caffeine LoadingCaches added to Spring CaffeineCacheManager](https://stackoverflow.com/questions/44507309/multiple-caffeine-loadingcaches-added-to-spring-caffeinecachemanager)

### 1. 背景

DIP平台主要提供两大核心功能:`人群服务`(圈选,放大,验证等)和`标签服务`(画像透视)。标签服务的核心就是提供画像实时OLAP服务，实现毫秒级别的画像透视。由于DIP平台每天产出的标签量巨大(数十个标签，十亿级别的量)，实时的OLAP查询在高并发下会产生**秒级**的延迟。通过调研实际的画像调用情况，可以发现画像透视查询在短时间内是有部分重复的，并且实际标签数据是天级别产出的（标签数据变化不频繁），因此对于画像透视查询可以在后台进行缓存来提高查询效率。

### 2. 画像透视缓存策略设计

先简单了解一下后文会涉及到的cache相关内容，cache可以设置多种策略，比如`expire`,`refresh`:

- expire: 过期策略，缓存如果超过expire时间，那么调用方法时将会阻塞直到返回结果（并缓存）
- refresh: 刷新策略，在未过期但满足refresh条件下进行调用，会用最新返回的查询结果来更新旧的缓存

根据以上两种策略，我们设计出一种适合DIP的画像透视缓存缓存策略：

```
1. 设置expire时间为7天，7天后缓存的数据将失效
2. 设置refresh为1天，在1天内数据不会被改变；在超过refresh时间，未超过expire时间情况下，在此期间调用相应方法，那么会先返回旧缓存值(并不被阻塞)，然后异步调用方法来更新缓存
```

必须要注意的：refresh机制必须**先返回旧缓存值，然后异步调用方法来更新缓存**(如果refresh时被相应方法所阻塞，那么该refresh实质上就成了expire)。

### 3. 基础实现

1. Spring框架本身就提供了对于cache机制的支持，因此首选依赖spring框架来实现DIP缓存策略（但实际上，spring无法提供refresh机制）
2. 在第一点的基础上，我们引入Caffeine，Caffeine提供的`expireAfterWrite`和`refreshAfterWrite`恰好能满足我们希望实现的所有需求，最重要的是Caffeine的refresh机制就是先返回旧值然后[异步刷新](https://github.com/ben-manes/caffeine/wiki/Refresh)
3. 好像结合Spring和Caffeine就能简单实现需求了，然而还是存在问题，使用Caffeine的refresh必须要实现`CacheLoader`,Caffeine使用CacheLoader的load方法来实现加载和reload方法实现refresh。在CacheLoader的具体实现内必须指明用于load的方法，所以当需要扩展时就会出现大量[冗余代码](https://stackoverflow.com/questions/44507309/multiple-caffeine-loadingcaches-added-to-spring-caffeinecachemanager)(每一个CacheLoader拥有一个只有细微差别的load实现)

先来看看最基础的实现:

#### 3.1 CacheConfig

```java
package com.example.demo.config;

import com.example.demo.repository.HelloRepository;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Profile("singleDemo")
@Slf4j
public class CacheConfig1 {
    private static String DELIMITED = "#$";

    @Autowired
    HelloRepository helloRepository;

    @Bean
    public CacheManager cache1() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        CaffeineCache c1 = new CaffeineCache("c1", Caffeine.newBuilder()
            // 10 min expire
            .expireAfterWrite(10, TimeUnit.MINUTES)
            // 10 sec refresh
            .refreshAfterWrite(10, TimeUnit.SECONDS)
            .build(key -> {
                // get class method param
                String[] info = StringUtils.delimitedListToStringArray((String) key, DELIMITED);
                return helloRepository.expensiveFunction1(info[2], info[3], false);
            }));

        simpleCacheManager.setCaches(
            Arrays.asList(c1)
        );
        return simpleCacheManager;
    }

    @Bean
    public MyKeyGenerator myKeyGenerator() {
        return new MyKeyGenerator();
    }

    public class MyKeyGenerator implements KeyGenerator {

        /**
         * @return 返回结果格式 class#;methodname#;params[0]#;params[1]...#;params[params.length-1]
         */
        @Override
        public Object generate(Object target, Method method, Object... params) {
            // params最后一个参数作为判断是否进行缓存的key,不参与生成key
            Object[] result = new Object[params.length + 1];
            result[0] = target.getClass().getName();
            result[1] = method.getName();
            for (int i = 0; i < params.length - 1; i++) {
                result[i + 2] = params[i];
            }
            String s = StringUtils.arrayToDelimitedString(result, DELIMITED);
            log.info(s);
            return s;
        }
    }
}
```

`CacheConfig`类提供生成`CacheManager`和`MyKeyGenerator`两个bean。

`MyKeyGenerator`：

MyKeyGenerator比较简单，就是生成cahce的key，生成规则就是class+name+params。但比较重要的一点是：params的最后一个参数不参与生成key(最后一个参数供Cacheable的condition来使用，后文会详细介绍原因)

`CacheManager`：

CacheManager拥有一个Caffeine，该Caffeine实现10分钟过期,10秒钟refresh。而build参数必须提供一个CacheLoader的实现，Caffeine通过这个loader用来获取实际的值并缓存。在CacheLoader实现中，调用的方法最后一个参数(enableCache)为false，表示关闭缓存。如果没有该参数的存在，在CacheLoader中调用helloRepository.expensiveFunction1()方法时会递归进入缓存，导致流程进入死循环！

> 我们也可以通过别的方式来实现避免递归进入缓存：实现两个方法，一个带有缓存注解供的方法a让外部使用，一个不带有缓存注解的方法b供Cache反射调用，方法a和b内容没有任何区别，代码显得相当冗余(丑陋)。

通过观察代码`return return helloRepository.expensiveFunction1(info[2], info[3], false);(info[2], info[3], false);`，我们也能很容易地能意识到：这个cache只能用来缓存`helloRepository.expensiveFunction1()`方法（如果将缓存加在其他方法上，最后调用该cache必然会得到错误的结果）。

#### 3.2 HelloRepository

```java
package com.example.demo.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Slf4j
public class HelloRepository {

    private AtomicInteger count = new AtomicInteger();

    /**
     * 需要缓存的耗时操作
     */
    @Cacheable(value = "c1", keyGenerator = "myKeyGenerator", cacheManager = "cache1", condition = "#enableCache==True")
    public int expensiveFunction1(String param1, String param2, boolean enableCache) {
        log.info("param1: " + param1 + ";param2: " + param2 + "enableCache" + enableCache);

        log.info("expensiveFunction1 in");
        int i = count.addAndGet(1);
        // sleep 5s
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("count: " + count);
        log.info("expensiveFunction1 out");
        return i;
    }
}
```

#### 3.3 HelloService

```java
package com.example.demo.service;

import com.example.demo.repository.HelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    HelloRepository helloRepository;

    public int f(String param1, String param2) {
        return helloRepository.expensiveFunction1(param1, param2, true);
    }
}
```

### 4. multi cache实现（冗余版）

基于前者方案，我们可以通过简单的代码copy实现多cache

#### 4.1 CacheConfig

```java
package com.example.demo.config;

import com.example.demo.repository.HelloRepository;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Profile("multiDemo")
@Slf4j
public class CacheConfig2 {
    private static String DELIMITED = "#$";

    @Autowired
    HelloRepository helloRepository;

    @Bean
    public CacheManager cache1() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        // cache1
        CaffeineCache c1 = new CaffeineCache("c1", Caffeine.newBuilder()
            // 10 min expire
            .expireAfterWrite(10, TimeUnit.MINUTES)
            // 10 sec refresh
            .refreshAfterWrite(10, TimeUnit.SECONDS)
            .build(key -> {
                // get class method param
                String[] info = StringUtils.delimitedListToStringArray((String) key, DELIMITED);
                return helloRepository.expensiveFunction1(info[2], info[3], false);
            }));

        // cache2
        CaffeineCache c2 = new CaffeineCache("c2", Caffeine.newBuilder()
            // 10 min expire
            .expireAfterWrite(10, TimeUnit.MINUTES)
            // 10 sec refresh
            .refreshAfterWrite(10, TimeUnit.SECONDS)
            .build(key -> {
                // get class method param
                String[] info = StringUtils.delimitedListToStringArray((String) key, DELIMITED);
                return helloRepository.expensiveFunction2(info[2], false);
            }));

        simpleCacheManager.setCaches(
            Arrays.asList(c1, c2)
        );
        return simpleCacheManager;
    }

    @Bean
    public MyKeyGenerator myKeyGenerator() {
        return new MyKeyGenerator();
    }

    public class MyKeyGenerator implements KeyGenerator {

        /**
         * @param target
         * @param method
         * @param params
         * @return 返回结果格式 class#;methodname#;params[0]#;params[1]...#;params[params.length-1]
         */
        @Override
        public Object generate(Object target, Method method, Object... params) {
            // params最后一个参数作为判断是否进行缓存的key,不参与生成key
            Object[] result = new Object[params.length + 1];
            result[0] = target.getClass().getName();
            result[1] = method.getName();
            for (int i = 0; i < params.length - 1; i++) {
                result[i + 2] = params[i];
            }
            String s = StringUtils.arrayToDelimitedString(result, DELIMITED);
            log.info(s);
            return s;
        }
    }
}
```

#### 4.2 HelloRepository

```java
package com.example.demo.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Slf4j
public class HelloRepository {

    private AtomicInteger count = new AtomicInteger();

    /**
     * 需要缓存的耗时操作
     */
    @Cacheable(value = "c1", keyGenerator = "myKeyGenerator", cacheManager = "cache1", condition = "#enableCache==True")
    public int expensiveFunction1(String param1, String param2, boolean enableCache) {
        log.info("param1: " + param1 + ";param2: " + param2 + ";enableCache: " + enableCache);

        log.info("expensiveFunction1 in");
        int i = count.addAndGet(1);
        // sleep 5s
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("count: " + count);
        log.info("expensiveFunction1 out");
        return i;
    }

    /**
     * 需要缓存的耗时操作2
     */
    @Cacheable(value = "c2", keyGenerator = "myKeyGenerator", cacheManager = "cache1", condition = "#enableCache==True")
    public int expensiveFunction2(String param1, boolean enableCache) {
        log.info("param1: " + param1 + ";enableCache: " + enableCache);

        log.info("expensiveFunction1 in");
        int i = count.addAndGet(1);
        // sleep 5s
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("count: " + count);
        log.info("expensiveFunction1 out");
        return i;
    }
}
```

#### 4.3 HelloService

```java
package com.example.demo.service;

import com.example.demo.repository.HelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    HelloRepository helloRepository;

    public int f(String param1, String param2) {
        return helloRepository.expensiveFunction1(param1, param2, true);
    }

    public int f2(String param1) {
        return helloRepository.expensiveFunction2(param1, true);
    }
}
```

上面的三个类基本和基础实现里的代码一致，只是从单个方法缓存演变成了多个方法缓存。仔细研究一下以上代码，不难发现代码里存在了大量的冗余(CaffeineCache c1和c2的实现几乎是一样的代码，只是load方法有所不同)。既然有冗余，那就继将冗余部分抽取出来通用化。

### 5. multi cache实现(reflect)

#### 5.1 CacheConfig

```java
package com.example.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
@Profile("reflectDemo")
@Slf4j
public class CacheConfig3 {
    private static String DELIMITED = "#$";

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public CacheManager cache1() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        List<CaffeineCache> cacheList = Arrays.asList("c1", "c2")
            .stream()
            .map(this::build)
            .collect(Collectors.toList());

        simpleCacheManager.setCaches(cacheList);
        return simpleCacheManager;
    }

    public CaffeineCache build(String name) {
        return new CaffeineCache(name, Caffeine.newBuilder()
            // 10 min expire
            .expireAfterWrite(10, TimeUnit.MINUTES)
            // 10 sec refresh
            .refreshAfterWrite(10, TimeUnit.SECONDS)
            .build(key -> {
                // get class method param
                String[] info = StringUtils.delimitedListToStringArray((String) key, DELIMITED);
                String clazzName = info[0];
                String methodName = info[1];
                // 参数,最后一个参数为 enableCache
                Object[] params = new Object[info.length - 1];
                int i = 0;
                for (; i < info.length - 2; i++) {
                    params[i] = info[i + 2];
                }
                params[i] = false;

                Class clazz = Class.forName(clazzName);
                Object bean = applicationContext.getBean(clazz);
                // 反射执行方法
                return MethodUtils.invokeMethod(bean, methodName, params);

            }));
    }

    @Bean
    public MyKeyGenerator myKeyGenerator() {
        return new MyKeyGenerator();
    }

    public class MyKeyGenerator implements KeyGenerator {

        /**
         * @param target
         * @param method
         * @param params
         * @return 返回结果格式 class#;methodname#;params[0]#;params[1]...#;params[params.length-1]
         */
        @Override
        public Object generate(Object target, Method method, Object... params) {
            // params最后一个参数作为判断是否进行缓存的key,不参与生成key
            Object[] result = new Object[params.length + 1];
            result[0] = target.getClass().getName();
            result[1] = method.getName();
            for (int i = 0; i < params.length - 1; i++) {
                result[i + 2] = params[i];
            }
            String s = StringUtils.arrayToDelimitedString(result, DELIMITED);
            log.info(s);
            return s;
        }
    }
}
```

只需要优化CacheConfig类，其他类保持不变即可，可以发现以下几点的改变：

1. 最新的CacheConfig类将CaffeineCache的构建抽象到了build方法内
2. CacheLoader的实现改成了反射方式：利用key的生成规则反解出class name,method name,param，并且新增参数enableCache=false；通过spring的ApplicationContext获取需要的bean，最后通过反射来执行method

### 6. 更多的改进？

至此，我们已经提出了相对通用化的解法：使用反射来减少了代码的冗余。不过在最后，还有一些点可以衍生：

1. 到目前为止，示例代码都是只提供了一个`CacheManager`，而该CacheManager内包含了多个Cache，每个Cache针对不同的方法进行缓存。而当我们使用反射来实现后，实际上，我们就已经能实现一个Cache对多个不同方法进行缓存(提供refresh)了。读者可以自行实现（其实几乎不用改多少代码）
2. 通过反射来实现虽然减少了代码的冗余，但无形之中影响了代码的执行效率，两者取舍应由具体场景而定
3. 在上文中并没有对key的生成规则进行详细的介绍，但这其实是十分重要的一点。由于CacheLoader的load方法只有一个参数key，所以我们必须将必要的信息(class，method，params)组合好传递给load方法，否则反射无法进行。由这个key出发，我们其实还有很多需要处理的地方：如果param并不是基本java类型，那么在key的生成过程中，我们要保证每个param的正确序列化；对于分隔符我们简单的使用了`#$`，如果参数中正好有这样的值，那么将导致反序列化出错，我们必须要进行转义处理以保证key的正确序列化和反序列化

本文相关源码[下载](https://github.com/htw0056/blog/tree/master/java/code/caffeine-async)

