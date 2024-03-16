package com.example.service2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private Environment environment;

    @GetMapping(value = "/")
    public String index() throws Exception {
        String port = environment.getProperty("server.port");

        return "hello service2! my port is :"+port;

    }

    @GetMapping(value = "/test")
    public String service1() throws Exception {
        String port = environment.getProperty("server.port");

        return "this is service2 test api";

    }
}
