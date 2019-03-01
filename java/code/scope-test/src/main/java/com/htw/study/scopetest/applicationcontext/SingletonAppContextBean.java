package com.htw.study.scopetest.applicationcontext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by htw on 2019/3/1.
 */
@Slf4j
@Component
public class SingletonAppContextBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    public SingletonAppContextBean() {
        log.info("Singleton instance created");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    public PrototypeBean getPrototypeBean() {
        return applicationContext.getBean(PrototypeBean.class);
    }
}
