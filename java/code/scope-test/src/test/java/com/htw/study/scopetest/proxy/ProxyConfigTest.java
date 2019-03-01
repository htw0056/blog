package com.htw.study.scopetest.proxy;

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
@ContextConfiguration(classes = {ProxyConfig.class})
public class ProxyConfigTest {

    @Autowired
    private SingletonProxyBean singletonProxyBean;

    @Test
    public void Test() {
        PrototypeBean prototypeBean1 = singletonProxyBean.getPrototypeBean();
        log.info("---------"+prototypeBean1.getCount());
        // 即使调用的是同一个引用，后台也会重新生成一个实例
        log.info("---------"+prototypeBean1.getCount());

        PrototypeBean prototypeBean2 = singletonProxyBean.getPrototypeBean();
        log.info("---------"+prototypeBean2.getCount());
        log.info("---------"+prototypeBean2.getCount());

        // 两者是相等的，因为返回的是一个代理类，只有真正调用的时候才会委托给真正的使用类
        Assert.assertEquals(prototypeBean1, prototypeBean2);

        /**
         * 输出
         * 15:57:20.063 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------1
         * 15:57:20.064 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------2
         * 15:57:20.064 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------3
         * 15:57:20.064 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------4
         *
         * 这里的输出和其他几个解决方案的输出不同
         * 原因是这里返回的是代理对象，每次调用代理对象方法都会重新委派给不同的实例
         */
    }

}