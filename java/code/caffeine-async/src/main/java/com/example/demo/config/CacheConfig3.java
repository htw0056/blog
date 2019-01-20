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

