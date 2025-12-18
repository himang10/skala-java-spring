package com.skala.basic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skala.basic.data.CourseResponse;
import com.skala.basic.data.HelloRequest;
import com.skala.basic.data.HelloResponse;
import com.skala.basic.service.CourseService;
import com.skala.basic.service.HelloService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HelloController {

  private final HelloService helloService;
  private final CourseService courseService;

  // 생성자 주입
  public HelloController(HelloService helloService, CourseService courseService) {
    this.helloService = helloService;
    this.courseService = courseService;
  }

  @GetMapping("/hello")
  public HelloResponse hello(@RequestParam(defaultValue = "SKALA") String name) {
    // 서비스 호출하여 응답 객체 생성
    log.info("/hello: GET {}", name);
    return helloService.createMessage(name);
  }

  @PostMapping("/hello")
  public HelloResponse postHello(@Valid @RequestBody HelloRequest body) {
    log.info("/hello: POST {}", body.getName());
    return helloService.createMessage(body.getName());
  }

  @PostMapping("/courses/{name}")
  public CourseResponse getClassInfo(@PathVariable String name, @RequestParam List<String> topics) {
    // Service에서 CourseResponse 생성
    log.info("/courses: {}", name);
    return courseService.createCourse(name, topics);
  }
}
