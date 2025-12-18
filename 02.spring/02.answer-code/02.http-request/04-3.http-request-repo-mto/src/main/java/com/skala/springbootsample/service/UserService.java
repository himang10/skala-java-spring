package com.skala.springbootsample.service;

import com.skala.springbootsample.domain.Region;
import com.skala.springbootsample.domain.User;
import com.skala.springbootsample.repo.RegionRepository;
import com.skala.springbootsample.repo.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    // 생성자 주입
    public UserService(UserRepository userRepository, RegionRepository regionRepository) {
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
    }

    // 이름 필터는 Service 계층에서 처리 (선택적)
    public List<User> findAll(Optional<String> name) {
        Collection<User> all = userRepository.findAll();
        if (name.isPresent()) {
            String search = name.get();
            return all.stream()
                    .filter(u -> u.getName() != null && u.getName().equalsIgnoreCase(search))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(all);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

        // 지역별 사용자 조회
    public List<User> findByRegionId(Long regionId) {
        return userRepository.findByRegionId(regionId);
    }

    // 지역명으로 사용자 조회
    public List<User> findByRegionName(String regionName) {
        return userRepository.findByRegionName(regionName);
    }

    //  ManyToOne 관계 추가로 인한 RegionId 추가
    @Transactional
    public User create(User user) {
        // 지역이 존재하는지 확인
        if (user.getRegion() != null && user.getRegion().getId() != null) {
            Region region = regionRepository.findById(user.getRegion().getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다: " + user.getRegion().getId()));
            user.setRegion(region);
        }

        return userRepository.save(user);
    }

    //  ManyToOne 관계 추가로 인한 RegionId 추가
    // 사용자 수정
    @Transactional
    public Optional<User> update(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());

                    // 지역 정보 업데이트
                    if (updatedUser.getRegion() != null && updatedUser.getRegion().getId() != null) {
                        Region region = regionRepository.findById(updatedUser.getRegion().getId())
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다: " + updatedUser.getRegion().getId()));
                        user.setRegion(region);
                    }

                    return userRepository.save(user);
                });
    }

    // 기존 boolean 시그니처 유지
    public boolean delete(long id) {
        Optional<User> existed = userRepository.findById(id);
        userRepository.deleteById(id); // 존재하지 않아도 무시
        return existed.isPresent();
    }
}


