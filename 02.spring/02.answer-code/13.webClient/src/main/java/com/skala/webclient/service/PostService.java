package com.skala.webclient.service;

import com.skala.webclient.dto.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * WebClient를 사용하여 JSONPlaceholder API를 호출하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final WebClient webClient;

    /**
     * Mono를 사용하여 단일 Post를 조회
     * GET /posts/{id}
     *
     * @param id Post ID
     * @return Map으로 감싼 Post (1개만 포함)
     */
    public Mono<Map<String, Post>> getPostByIdWithMono(Long id) {
        log.info("Mono를 사용하여 Post 조회: ID = {}", id);
        
        return webClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .bodyToMono(Post.class)
                .map(post -> {
                    Map<String, Post> resultMap = new HashMap<>();
                    resultMap.put("post", post);
                    return resultMap;
                })
                .doOnSuccess(result -> log.info("Mono 조회 성공: {}", result))
                .doOnError(error -> log.error("Mono 조회 실패", error));
    }

    /**
     * Flux를 사용하여 여러 Post를 조회하여 Flux로 반환
     * GET /posts
     *
     * @return Flux<Post> 스트림
     */
    public Flux<Post> getPostsWithFlux() {
        log.info("Flux를 사용하여 Posts 조회");
        
        return webClient.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(Post.class)
                .doOnNext(post -> log.debug("Post 수신: {}", post.getId()))
                .doOnComplete(() -> log.info("Flux 조회 완료"))
                .doOnError(error -> log.error("Flux 조회 실패", error));
    }
}
