package com.example.service1.controller;

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

        return "hello service1! my port is :"+port;

    }

    @GetMapping(value = "/test")
    public String service1() throws Exception {
        String port = environment.getProperty("server.port");

        return "this is service1 test api";

    }
}
