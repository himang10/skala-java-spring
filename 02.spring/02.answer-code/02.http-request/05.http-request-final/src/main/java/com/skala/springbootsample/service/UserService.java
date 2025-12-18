package com.skala.springbootsample.service;

import com.skala.springbootsample.domain.User;
import com.skala.springbootsample.domain.Region;
import com.skala.springbootsample.repo.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    // 모든 사용자 조회 (이름 필터 옵션)
    public List<User> findAll(Optional<String> name) {
        if (name.isPresent()) {
            return userRepository.findByNameIgnoreCase(name.get());
        }
        return userRepository.findAll();
    }

    // ID로 사용자 조회
    public Optional<User> findById(Long id) {
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

    // 사용자 생성
    @Transactional
    public User create(User user) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + user.getEmail());
        }

        // 지역이 존재하는지 확인
        if (user.getRegion() != null && user.getRegion().getId() != null) {
            Region region = regionRepository.findById(user.getRegion().getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다: " + user.getRegion().getId()));
            user.setRegion(region);
        }

        return userRepository.save(user);
    }

    // 사용자 수정
    @Transactional
    public Optional<User> update(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());

                    // 이메일 변경 시 중복 체크 (자기 자신 제외)
                    if (!user.getEmail().equals(updatedUser.getEmail())) {
                        if (userRepository.existsByEmail(updatedUser.getEmail())) {
                            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + updatedUser.getEmail());
                        }
                        user.setEmail(updatedUser.getEmail());
                    }

                    // 지역 정보 업데이트
                    if (updatedUser.getRegion() != null && updatedUser.getRegion().getId() != null) {
                        Region region = regionRepository.findById(updatedUser.getRegion().getId())
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다: " + updatedUser.getRegion().getId()));
                        user.setRegion(region);
                    }

                    return userRepository.save(user);
                });
    }

    // 사용자 삭제
    @Transactional
    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
