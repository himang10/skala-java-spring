package com.sk.skala.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class HellloController {

  @GetMapping("/hello")
  public HelloResponse hello() {
    HelloResponse response = new HelloResponse();
    response.setMessage("SKALA에 오신 것을 환영합니다.");
    return response;
  }

}
