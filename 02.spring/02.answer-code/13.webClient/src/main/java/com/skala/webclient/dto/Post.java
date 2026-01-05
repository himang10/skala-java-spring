package com.skala.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSONPlaceholder API의 Post 응답을 받기 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long userId;
    private Long id;
    private String title;
    private String body;
}
