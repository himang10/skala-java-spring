package com.skala.springbootsample.repo;

import com.skala.springbootsample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이름으로 사용자 검색 (대소문자 구분 없음)
    List<User> findByNameIgnoreCase(String name);

    // 지역별 사용자 조회
    List<User> findByRegionId(Long regionId);

    // 지역명으로 사용자 조회 (Spring Data JPA 메서드 네이밍 규칙 사용)
    List<User> findByRegionName(String regionName);

    // 이메일로 사용자 존재 여부 확인
    boolean existsByEmail(String email);

}
