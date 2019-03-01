package com.htw.study.scopetest.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by htw on 2019/3/1.
 */
@Slf4j
@Component
public class SingletonBean {

    @Autowired
    private PrototypeBean prototypeBean;

    public SingletonBean() {
        log.info("Singleton instance created");
    }

    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }
}
