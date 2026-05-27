package com.skala.webclient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebClient 설정 클래스
 */
@Configuration
public class WebClientConfig {

    @Value("${webclient.base-url}")
    private String baseUrl;

    @Value("${webclient.timeout}")
    private int timeout;

    @Bean
    public WebClient webClient() {
        // HttpClient 설정 (타임아웃 등)
        HttpClient httpClient = HttpClient.create()
                // 서버 연결 수립 제한 시간
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                // 전체 응답 완료 제한 시간
                .responseTimeout(Duration.ofMillis(timeout))
                // 연결 성공 시 핸들러 추가
                .doOnConnected(conn ->
                        // 데이터 읽기 제한 시간 (서버 응답 지연 방지)
                        conn.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS))
                                // 데이터 쓰기 제한 시간 (요청 전송 지연 방지)
                                .addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
