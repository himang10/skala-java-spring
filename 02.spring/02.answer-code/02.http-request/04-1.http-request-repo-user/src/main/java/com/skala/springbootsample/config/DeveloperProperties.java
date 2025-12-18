package com.skala.springbootsample.config;

import com.skala.springbootsample.dto.Owner;
import com.skala.springbootsample.dto.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "developer")
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperProperties {

    private Owner owner ;
    private Team team;
}

