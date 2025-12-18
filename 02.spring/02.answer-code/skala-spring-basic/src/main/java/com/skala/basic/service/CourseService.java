package com.skala.basic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skala.basic.data.CourseResponse;
import com.skala.basic.table.Course;
import com.skala.basic.repository.CourseRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor // 생성자 주입 자동 생성
public class CourseService {

  private final CourseRepository courseRepository;

  public CourseResponse createCourse(String name, List<String> topics) {
    log.info("topics-size={}", topics.size());

    // topics 리스트를 콤마로 구분된 문자열로 변환
    String topic = String.join(",", topics);
    String desc = String.format("%s님이 관심 있는 분야: %s", name, String.join(", ", topics));

    // Course 엔터티 생성
    Course course = new Course();
    course.setName(name);
    course.setTopic(topic);
    course.setDescription(desc);

    // H2 DB에 저장
    Course savedCourse = courseRepository.save(course);

    // 응답 객체 생성
    CourseResponse response = new CourseResponse();
    response.setName(savedCourse.getName());
    response.setTopics(topics); // 원본 List로 반환
    response.setDescription(savedCourse.getDescription());

    return response;
  }
}
