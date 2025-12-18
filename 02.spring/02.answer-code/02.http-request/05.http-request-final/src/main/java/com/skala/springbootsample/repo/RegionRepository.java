package com.skala.springbootsample.repo;

import com.skala.springbootsample.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    // 지역명으로 조회
    Optional<Region> findByName(String name);

    // 지역명 존재 여부 확인
    boolean existsByName(String name);
}
