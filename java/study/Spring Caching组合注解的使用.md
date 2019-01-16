# Spring Caching组合注解的使用

### 1. 背景

用户画像数据在短时间内不会骤变（画像数据至少T+1产出），因此适当地使用缓存技术能极大地提高查询效率和降低服务器压力。而在某些特殊情况下，使用方会希望能获取最新的数据，因此我们必须为用户提供参数来控制缓存策略。默认情况下，用户的请求会被缓存，而当用户在接口中填写`强制刷新缓存参数`后，后台必须要获取最新的数据并更新缓存，简化后的场景如下：

```
对于方法 f(String a,int b) ，需要根据参数b的值来执行不同的缓存操作:
1. 当b==1,则方法f执行缓存策略，如果已经有缓存则直接返回，如果没缓存则执行f并缓存
2. 当b!=1,则执行方法f并更新缓存(无缓存则新增)
```

对于以上的场景，其实正好和Spring cache提供的两个注解完美契合。第一种情况对应于`@Cacheable`,第二种对应于`@CachePut`。因此我们很自然地得到第一种解决方法。

### 2. 方法一: Cacheable+CachePut

```java
package com.example.demo.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Slf4j
public class HelloRepository {

    private AtomicInteger count = new AtomicInteger();
    /**
     * 走缓存
     */
    @Cacheable(value = "cache1", key = "#param", cacheManager = "cacheManager")
    public int f1(String param) {
        int i = count.addAndGet(1);
        log.info("count" + count);
        return i;
    }
    /**
     * 更新缓存
     */
    @CachePut(value = "cache1", key = "#param", cacheManager = "cacheManager")
    public int f2(String param) {
        int i = count.addAndGet(1);
        log.info("count" + count);
        return i;
    }

}

```

```java
package com.example.demo.service;

import com.example.demo.repository.HelloRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloService {
    @Autowired
    HelloRepository helloRepository;

    public int f(String a, int b) {
        if (b == 1) {
            return helloRepository.f1(a);
        } else {
            return helloRepository.f2(a);
        }
    }
}

```

上面两个类很简单，`HelloService`的方法f根据b的值不同调用`HelloRepository`的不同方法。而`HelloRepository`的f1()和f2()，分别使用了Cacheable和CachePut两个不同的注解。

以上方式就解决了我们前面提到的问题，不同情况下使用不同的缓存策略。但是，这样总觉得还不够好，至少不够美观。比如:

1. 实际上f1()和f2()包含的代码是完全一样的，只是带有的注解不同而已，代码存在冗余(可以再从f1,f2中extract出一个方法，实际上并没降低问题的复杂度)

2. f()方法里存在if else判断以执行不同的方法，如果我们的缓存策略变得更复杂了呢？那代码里就充满了if判断语句

### 2. 方法二: Caching+Cacheable+CachePut

实际上我们需要的只是一个方法上能实现多个注解，所以Caching注解就为我们提供了一种更优化的解决方式。（在某些情况下，并不建议使用Caching注解，后文会提到）

```java
package com.example.demo.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Slf4j
public class HelloRepository {

    private AtomicInteger count = new AtomicInteger();

    /**
     * 组合注解
     */
    @Caching(
        cacheable = {@Cacheable(value = "cache1", key = "#param", cacheManager = "cacheManager", condition = "#type==1")},
        put = {@CachePut(value = "cache1", key = "#param", cacheManager = "cacheManager", condition = "#type!=1")}
    )
    public int f3(String param, int type) {
        int i = count.addAndGet(1);
        log.info("count" + count);
        return i;
    }

}
```

```java
package com.example.demo.service;

import com.example.demo.repository.HelloRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloService {
    @Autowired
    HelloRepository helloRepository;

    public int fNew(String a, int b) {
        return helloRepository.f3(a, b);
    }
}

```

我们可以看到，新的实现方式直接将参数b传递给`HelloRepository`的方法f3(),f3()使用组合注解，根据condition判断执行不同的缓存策略。和第一种方式相比，代码显得更加简洁，可读。

### 3. 是否该用Caching

按照上面的两种方法对比，好像使用Caching没有缺点？实际上并不是的。

稍微修改一下我们的Caching注解内容:

```java
// condition = "#type!=1" ->  condition = "#type>0"
@Caching(
        cacheable = {@Cacheable(value = "cache1", key = "#param", cacheManager = "cacheManager", condition = "#type==1")},
        put = {@CachePut(value = "cache1", key = "#param", cacheManager = "cacheManager", condition = "#type>0")}
    ) 
```

当type=1时，你觉得缓存策略会怎么选择？两个策略condition有了交集，到底是`Cacheable`生效还是`CachePut`生效?（我也不知道）

[Spring官方文档](https://docs.spring.io/spring/docs/5.1.4.RELEASE/spring-framework-reference/integration.html#cache-annotations-put)特地进行了提示:

> Using `@CachePut` and `@Cacheable` annotations on the same method is generally strongly discouraged because they have different behaviors. While the latter causes the method execution to be skipped by using the cache, the former forces the execution in order to execute a cache update. This leads to unexpected behavior and, with the exception of specific corner-cases (such as annotations having conditions that exclude them from each other), such declarations should be avoided. Note also that such conditions should not rely on the result object (that is, the `#result` variable), as these are validated up-front to confirm the exclusion.

所以，如果要使用Caching来组合`CachePut`和`Cacheable`两个注解时，一定要小心，保证condition条件必须为互斥条件！

### 4. 结语

实际上，解决以上问题的方式还有很多，但孰优孰劣需要具体情况来分析。如果各位还有更好的解决方法，欢迎留言讨论。

[代码](https://github.com/htw0056/blog/tree/master/java/code/cache-test)

