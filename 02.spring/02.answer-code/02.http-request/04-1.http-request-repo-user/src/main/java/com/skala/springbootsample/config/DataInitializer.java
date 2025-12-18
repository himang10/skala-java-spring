package com.skala.springbootsample.config;

import com.skala.springbootsample.domain.User;
import com.skala.springbootsample.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("초기 데이터 설정 시작");

        // 사용자 데이터 생성
        userRepository.save(new User(null, "alice", "alice@example.com"));
        userRepository.save(new User(null, "bob", "bob@example.com"));
        userRepository.save(new User(null, "charlie", "charlie@example.com"));

        log.info("초기 데이터 설정 완료");
    }
}

