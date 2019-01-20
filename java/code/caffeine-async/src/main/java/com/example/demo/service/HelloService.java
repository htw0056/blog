package com.example.demo.service;

import com.example.demo.repository.HelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    HelloRepository helloRepository;

    public int f(String param1, String param2) {
        return helloRepository.expensiveFunction1(param1, param2, true);
    }

    public int f2(String param1) {
        return helloRepository.expensiveFunction2(param1, true);
    }
}
