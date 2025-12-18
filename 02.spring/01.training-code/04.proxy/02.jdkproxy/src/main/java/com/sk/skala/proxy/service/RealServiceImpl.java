package com.sk.skala.proxy.service;


public class RealServiceImpl implements MyService {
    @Override
    public String getMessage(String name) {
        return "Hello, " + name;
    }
}
