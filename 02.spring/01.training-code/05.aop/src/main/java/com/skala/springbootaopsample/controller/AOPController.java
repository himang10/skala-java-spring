package com.skala.springbootaopsample.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skala.springbootaopsample.config.MyInfoProperties;


@RestController
@RequestMapping("/api")
public class AOPController {
    private final MyInfoProperties myInfoProperties;

    @Value("${spring.application.name}")
    private String appName;

    public AOPController(MyInfoProperties myInfoProperties) {
        this.myInfoProperties = myInfoProperties;
    }

    @GetMapping("/info")
    public String getRequestInfo(RequestEntity<String> requestEntity) {

        String request = "request: Header: " + requestEntity.getHeaders() + "Body: " + requestEntity.getBody();

        System.out.println("\n\n--> Executing getMethodName method in AOPController. \n\n");

        return request;
    }

    @GetMapping("/hello")
    public String hello() {
        System.out.println("\n\n --> hello controller method called \n\n");
        return "Hello World!";
    }

    // properties 정보를 반환하는 API
    @GetMapping("/myinfo")
    public MyInfoProperties getMyInfo() {
        System.out.println("\n\n --> myinfo method called \n\n");
        return myInfoProperties;
    }

    // spring.application.name properties를 반환하는 API
    @GetMapping("/name")
    public String getName() {
        System.out.println("\n\n --> name method called \n\n");
        return appName;
    }

}
