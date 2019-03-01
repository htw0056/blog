package com.htw.study.scopetest.proxy;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by htw on 2019/3/1.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
    proxyMode = ScopedProxyMode.TARGET_CLASS)
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
