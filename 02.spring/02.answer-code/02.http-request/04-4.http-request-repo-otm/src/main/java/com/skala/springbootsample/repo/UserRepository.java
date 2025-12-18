package com.skala.springbootsample.repo;

import com.skala.springbootsample.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    // 지역별 사용자 조회
    List<User> findByRegionId(Long regionId);

    // 지역명으로 사용자 조회 (Spring Data JPA 메서드 네이밍 규칙 사용)
    List<User> findByRegionName(String regionName);
}

