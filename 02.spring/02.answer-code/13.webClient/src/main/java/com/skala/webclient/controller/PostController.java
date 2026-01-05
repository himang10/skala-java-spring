package com.skala.webclient.controller;

import com.skala.webclient.dto.Post;
import com.skala.webclient.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * WebClient를 테스트하기 위한 REST Controller
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Mono를 사용하여 단일 Post 조회
     * 
     * @param id Post ID
     * @return Map으로 감싼 Post
     */
    @GetMapping("/mono/{id}")
    public Mono<Map<String, Post>> getPostWithMono(@PathVariable Long id) {
        log.info("GET /api/mono/{} 호출", id);
        return postService.getPostByIdWithMono(id);
    }

    /**
     * Flux를 사용하여 Posts 조회 (Flux 스트림으로 반환)
     * 
     * @return Flux<Post> 스트림
     */
    @GetMapping("/flux")
    public Flux<Post> getPostsWithFlux() {
        log.info("GET /api/flux 호출");
        return postService.getPostsWithFlux();
    }
}
