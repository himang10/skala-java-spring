package com.skala.examples.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skala.examples.domain.User;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private List<User> users = new ArrayList<>(List.of(
        new User(1, "alice", 20, "alice@example.com"),
        new User(2, "bob", 30, "bob@example.com"),
        new User(3, "charlie", 32, "charlie@example.com")
    ));
    private long userIdCounter = 1;
    

    // 모든 사용자 조회 및 특정 사용자 이름으로 필터링
    @GetMapping
    public List<User> getAllUsers(@RequestParam Optional<String> name) {
        if (name.isPresent()) {
            String searchName = name.get();
            // 이름으로 필터링
            return users.stream()
                    .filter(user -> user.getName().equalsIgnoreCase(searchName))
                    .toList();
        }
        return users; // 모든 사용자 반환
    }

    // GET: 특정 사용자 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: 사용자 추가
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        user.setId(userIdCounter++);
        users.add(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // DELETE: 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        User userToRemove = users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
        if (userToRemove != null) {
            users.remove(userToRemove);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    // PUT: 사용자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        // 기존 사용자 찾기
        Optional<User> existingUser = users.stream()
            .filter(user -> user.getId() == id)
            .findFirst();

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // 새로운 정보로 업데이트 (id는 유지)
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            // 필요한 다른 필드들도 업데이트

            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Valid 문제 발생 시 처리하는 Exception Handler 정의
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        System.out.println("[MethodArgumentNotValidException ExceptionalHandler] Validation Error: ");
        

        return ResponseEntity.status(500).body(ex.getMessage());
    }

    /**
     * 그 외 모든 예외를 처리하는 Exception Handler 정의
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        System.out.println("[RunTimeException ExceptionalHandler] Runtime Error: ");

        return ResponseEntity.status(500).body(ex.getMessage());
    }

}
