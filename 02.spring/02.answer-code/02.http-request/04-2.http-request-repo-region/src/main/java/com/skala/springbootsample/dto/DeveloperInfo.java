package com.skala.springbootsample.dto;

import com.skala.springbootsample.config.DeveloperProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// Java 17 record 기반, 계층 구조를 Properties와 동일하게
@Data
@AllArgsConstructor
public class DeveloperInfo {
    private Owner owner;
    private Team team;
}

