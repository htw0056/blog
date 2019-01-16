package com.example.demo.service;

import com.example.demo.repository.HelloRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by htw on 2019/1/16.
 */
@Service
@Slf4j
public class HelloService {
    @Autowired
    HelloRepository helloRepository;

    public int f(String a, int b) {
        if (b == 1) {
            return helloRepository.f1(a);
        } else {
            return helloRepository.f2(a);
        }
    }

    public int fNew(String a, int b) {
        return helloRepository.f3(a, b);
    }

}
