package com.htw.study.scopetest.objectfactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by htw on 2019/3/1.
 */
@Slf4j
@Component
public class SingletonObjectFactoryBean {

    public SingletonObjectFactoryBean() {
        log.info("Singleton instance created");
    }

    @Autowired
    private ObjectFactory<PrototypeBean> prototypeBeanObjectFactory;

    public PrototypeBean getPrototypeBean() {
        return prototypeBeanObjectFactory.getObject();
    }

}
