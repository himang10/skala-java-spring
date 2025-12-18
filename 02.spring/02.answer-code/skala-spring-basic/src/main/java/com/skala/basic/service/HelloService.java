package com.skala.basic.service;

import org.springframework.stereotype.Service;

import com.skala.basic.data.HelloResponse;

@Service
public class HelloService {

  public HelloResponse createMessage(String name) {
    HelloResponse response = new HelloResponse();
    response.setMessage("안녕하세요, " + name + "님!");
    return response;
  }
}
