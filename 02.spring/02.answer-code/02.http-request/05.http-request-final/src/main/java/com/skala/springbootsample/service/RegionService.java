package com.skala.springbootsample.service;

import com.skala.springbootsample.domain.Region;
import com.skala.springbootsample.repo.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    // 모든 지역 조회
    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    // ID로 지역 조회
    public Optional<Region> findById(Long id) {
        return regionRepository.findById(id);
    }

    // 지역명으로 조회
    public Optional<Region> findByName(String name) {
        return regionRepository.findByName(name);
    }

    // 지역 생성
    @Transactional
    public Region create(Region region) {
        if (regionRepository.existsByName(region.getName())) {
            throw new IllegalArgumentException("이미 존재하는 지역명입니다: " + region.getName());
        }
        return regionRepository.save(region);
    }

    // 지역 수정
    @Transactional
    public Optional<Region> update(Long id, Region updatedRegion) {
        return regionRepository.findById(id)
                .map(region -> {
                    // 지역명 변경 시 중복 체크 (자기 자신 제외)
                    if (!region.getName().equals(updatedRegion.getName())) {
                        if (regionRepository.existsByName(updatedRegion.getName())) {
                            throw new IllegalArgumentException("이미 존재하는 지역명입니다: " + updatedRegion.getName());
                        }
                        region.setName(updatedRegion.getName());
                    }
                    return regionRepository.save(region);
                });
    }

    // 지역 삭제
    @Transactional
    public boolean delete(Long id) {
        if (regionRepository.existsById(id)) {
            regionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
