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
