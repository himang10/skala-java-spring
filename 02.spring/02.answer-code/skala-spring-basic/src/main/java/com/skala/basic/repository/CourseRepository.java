package com.skala.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.skala.basic.table.Course;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  // 이름으로 조회하는 메서드 예시
  List<Course> findByName(String name);

  // 특정 토픽을 포함하는 코스 조회 (콤마로 구분된 문자열이므로 like 쿼리 사용)
  List<Course> findByTopicContaining(String topic);
}
