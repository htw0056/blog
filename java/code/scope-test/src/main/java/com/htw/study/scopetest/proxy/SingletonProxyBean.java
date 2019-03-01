package com.htw.study.scopetest.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by htw on 2019/3/1.
 */
@Slf4j
@Component
public class SingletonProxyBean {

    @Autowired
    private PrototypeBean prototypeBean;

    public SingletonProxyBean() {
        log.info("Singleton instance created");
    }

    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }
}
