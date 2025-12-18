package com.skala.springbootsample.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class LifecycleBean {

    public LifecycleBean() {
        System.out.println("[LifecycleBean] 생성자 호출됨 (Bean 생성)");
    }

    @PostConstruct
    public void init() {
        System.out.println("[LifecycleBean] @PostConstruct 호출됨 (초기화)");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[LifecycleBean] @PreDestroy 호출됨 (소멸 직전)");
    }
}

