package com.skala.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skala.myapp.service.UserService;
import com.skala.myapp.service.UserServiceImpl;
import com.skala.myapp.service.UserServiceProxy;
import com.skala.myapp.repo.FileUserRepository;

@Configuration
public class AppConfig {
    @Bean
    public UserService userService(FileUserRepository userRepository) {
        // 실제 대상 생성
        UserService target = new UserServiceImpl(userRepository);
        // 프록시로 감싸서 반환
        return new UserServiceProxy(target);
    }
}
