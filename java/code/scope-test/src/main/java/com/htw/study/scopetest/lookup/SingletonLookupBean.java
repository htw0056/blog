package com.htw.study.scopetest.lookup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * Created by htw on 2019/3/1.
 */
@Slf4j
@Component
public class SingletonLookupBean {


    public SingletonLookupBean() {
        log.info("Singleton instance created");
    }

    @Lookup
    public PrototypeBean getPrototypeBean() {
        return null;
    }
}
