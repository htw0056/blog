package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by htw on 2019/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("multiDemo")
public class HelloControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello1() throws Exception {
        // 第1次执行,spend time 应该很大 > 5000
        log.info("begin1: " + System.currentTimeMillis());
        long begin = System.currentTimeMillis();
        mockMvc.perform(get("/method1/a/1"))
//            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        long end = System.currentTimeMillis();
        log.info("end1: " + System.currentTimeMillis());
        log.info("spend time1: " + (end - begin));

        // 第2次执行,未过期,未refresh
        log.info("begin2: " + System.currentTimeMillis());
        begin = System.currentTimeMillis();
        mockMvc.perform(get("/method1/a/1"))
//            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        end = System.currentTimeMillis();
        log.info("end2: " + System.currentTimeMillis());
        log.info("spend time2: " + (end - begin));

        // sleep 15s,达到refresh要求,此时异步返回旧值1,spend time 应该很小
        Thread.sleep(1000 * 15);
        log.info("begin3: " + System.currentTimeMillis());
        begin = System.currentTimeMillis();
        mockMvc.perform(get("/method1/a/1"))
//            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        end = System.currentTimeMillis();
        log.info("end3: " + System.currentTimeMillis());
        log.info("spend time3: " + (end - begin));

        // sleep 6s,保证refresh完成,再次访问,得到刷新后的值2
        Thread.sleep(1000 * 6);
        log.info("begin4: " + System.currentTimeMillis());
        begin = System.currentTimeMillis();
        mockMvc.perform(get("/method1/a/1"))
//            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("2")));
        end = System.currentTimeMillis();
        log.info("end4: " + System.currentTimeMillis());
        log.info("spend time4: " + (end - begin));
    }


    @Test
    public void hello2() throws Exception {

    }
}