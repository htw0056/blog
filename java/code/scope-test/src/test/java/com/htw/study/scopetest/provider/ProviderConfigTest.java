package com.htw.study.scopetest.provider;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by htw on 2019/3/1.
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ProviderConfig.class})
public class ProviderConfigTest {

    @Autowired
    private SingletonProviderBean singletonProviderBean;

    @Test
    public void Test() {
        PrototypeBean prototypeBean1 = singletonProviderBean.getPrototypeBean();
        log.info("---------"+prototypeBean1.getCount());
        log.info("---------"+prototypeBean1.getCount());
        PrototypeBean prototypeBean2 = singletonProviderBean.getPrototypeBean();
        log.info("---------"+prototypeBean2.getCount());
        log.info("---------"+prototypeBean2.getCount());
        // 两次获得的bean不同
        Assert.assertNotEquals(prototypeBean1, prototypeBean2);

        /**
         * 输出
         * 15:46:05.028 [main] INFO com.htw.study.scopetest.applicationcontext.ApplicationContextConfigTest - ---------1
         * 15:46:05.028 [main] INFO com.htw.study.scopetest.applicationcontext.ApplicationContextConfigTest - ---------1
         * 15:46:05.028 [main] INFO com.htw.study.scopetest.applicationcontext.ApplicationContextConfigTest - ---------2
         * 15:46:05.029 [main] INFO com.htw.study.scopetest.applicationcontext.ApplicationContextConfigTest - ---------2
         *
         * 实例化两个PrototypeBean
         */
    }
}