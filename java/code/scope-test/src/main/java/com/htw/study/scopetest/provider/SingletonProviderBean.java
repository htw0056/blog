package com.htw.study.scopetest.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Provider;


/**
 * Created by htw on 2019/3/1.
 */
@Slf4j
@Component
public class SingletonProviderBean {

    public SingletonProviderBean() {
        log.info("Singleton instance created");
    }

    @Autowired
    private Provider<PrototypeBean> prototypeBeanObjectFactory;

    public PrototypeBean getPrototypeBean() {
        return prototypeBeanObjectFactory.get();
    }

}
