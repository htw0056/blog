package com.example.demo.controller;

import com.example.demo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by htw on 2019/1/18.
 */
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @GetMapping("/method1/{key1}/{key2}")
    public String hello1(@PathVariable(name = "key1") String key1, @PathVariable(name = "key2") String key2) {
        return "" + helloService.f(key1, key2);
    }

    @GetMapping("/method2/{key1}")
    public String hello2(@PathVariable(name = "key1") String key1) {
        return "" + helloService.f2(key1);
    }
}
