package com.example.demo.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by htw on 2019/1/16.
 */
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
