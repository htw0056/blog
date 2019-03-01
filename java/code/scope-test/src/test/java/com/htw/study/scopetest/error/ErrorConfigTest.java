package com.htw.study.scopetest.error;

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
@ContextConfiguration(classes = {ErrorConfig.class})
public class ErrorConfigTest {

    @Autowired
    private SingletonBean singletonBean;

    @Test
    public void Test() {
        PrototypeBean prototypeBean1 = singletonBean.getPrototypeBean();
        log.info("---------"+prototypeBean1.getCount());
        log.info("---------"+prototypeBean1.getCount());
        PrototypeBean prototypeBean2 = singletonBean.getPrototypeBean();
        log.info("---------"+prototypeBean2.getCount());
        log.info("---------"+prototypeBean2.getCount());
        // 获得的是同一个bean，因为 prototypeBean 是在singletonBean构造时注入，即使prototypeBean作用域是prototype
        // 而singletonBean是单例，只实例化一次，因此直接在单例中注入prototype作用域bean不能起到每次新生成prototypeBean
        Assert.assertEquals(prototypeBean1,prototypeBean2);

        /**
         * 输出
         * 15:42:33.300 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         * 15:42:33.300 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         * 15:42:33.301 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         * 15:42:33.301 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         *
         * 这也能说明只实例化一次
         */
    }

}