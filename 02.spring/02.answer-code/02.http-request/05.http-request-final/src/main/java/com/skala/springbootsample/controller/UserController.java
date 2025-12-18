package com.skala.springbootsample.controller;

import java.util.List;
import java.util.Optional;

import com.skala.springbootsample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skala.springbootsample.domain.User;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 모든 사용자 조회 및 이름 필터
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam Optional<String> name) {
        try {
            List<User> users = userService.findAll(name);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 사용자 조회
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("getUserById called with id: {}", id);
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 지역별 사용자 조회
    @GetMapping("/users/region/{regionId}")
    public ResponseEntity<List<User>> getUsersByRegionId(@PathVariable Long regionId) {
        try {
            List<User> users = userService.findByRegionId(regionId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("지역별 사용자 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 지역명으로 사용자 조회
    @GetMapping("/users/region-name/{regionName}")
    public ResponseEntity<List<User>> getUsersByRegionName(@PathVariable String regionName) {
        try {
            List<User> users = userService.findByRegionName(regionName);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("지역명별 사용자 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 생성
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.create(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.warn("사용자 생성 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("사용자 생성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 수정
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            return userService.update(id, updatedUser)
                    .map(user -> ResponseEntity.ok(user))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            log.warn("사용자 수정 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("사용자 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            return userService.delete(id) ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("사용자 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
