package com.htw.study.scopetest.provider;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by htw on 2019/3/1.
 */
@Component
@Scope("prototype")
public class PrototypeBean {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);

    // 每次初始化+1
    public PrototypeBean() {
        instanceCounter.incrementAndGet();
    }

    public Integer getCount() {
        return instanceCounter.get();
    }
}
