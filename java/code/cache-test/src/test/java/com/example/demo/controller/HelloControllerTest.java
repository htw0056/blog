package com.example.demo.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by htw on 2019/1/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello1() throws Exception {
        // 第一次返回1
        mockMvc.perform(get("/method1/a/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        // 继续走缓存
        mockMvc.perform(get("/method1/a/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        // 继续走缓存
        mockMvc.perform(get("/method1/b/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("2")));
        // 使用参数2,强制刷新缓存
        mockMvc.perform(get("/method1/a/2"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("3")));
        // 重新调用缓存方法,返回新的缓存结果
        mockMvc.perform(get("/method1/a/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("3")));
    }

    @Test
    public void hello2() throws Exception {
        // 第一次返回1
        mockMvc.perform(get("/method2/a/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        // 继续走缓存
        mockMvc.perform(get("/method2/a/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")));
        // 不同的可以缓存
        mockMvc.perform(get("/method2/b/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("2")));
        // 使用参数2,强制刷新缓存
        mockMvc.perform(get("/method2/a/2"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("3")));
        // 重新调用缓存方法,返回新的缓存结果
        mockMvc.perform(get("/method2/a/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("3")));
    }
}