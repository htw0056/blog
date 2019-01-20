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

/*                // class
                String clazz = info[0];
                // method
                String methodName = info[1];*/
