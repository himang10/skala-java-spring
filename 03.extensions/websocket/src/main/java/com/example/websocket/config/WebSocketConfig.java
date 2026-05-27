package com.example.websocket.config;

import com.example.websocket.handler.EduWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final EduWebSocketHandler eduWebSocketHandler;

    public WebSocketConfig(EduWebSocketHandler eduWebSocketHandler) {
        this.eduWebSocketHandler = eduWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(eduWebSocketHandler, "/ws/connect")
                .setAllowedOriginPatterns("*");
    }
}
