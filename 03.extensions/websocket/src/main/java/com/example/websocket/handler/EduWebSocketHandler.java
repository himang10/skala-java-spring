package com.example.websocket.handler;

import com.example.websocket.service.WebSocketSessionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class EduWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(EduWebSocketHandler.class);
    private static final String ATTR_CLIENT_ID = "clientId";

    private final WebSocketSessionService sessionService;
    private final ObjectMapper objectMapper;

    public EduWebSocketHandler(WebSocketSessionService sessionService, ObjectMapper objectMapper) {
        this.sessionService = sessionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String clientId = resolveClientId(session.getUri());
        session.getAttributes().put(ATTR_CLIENT_ID, clientId);
        sessionService.registerSession(clientId, session);

        sessionService.sendToClient(clientId, "connected", "handshake complete", "server");

        log.info("[STEP1] handshake done -> HTTP 101 Upgrade complete for clientId={}", clientId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String clientId = getClientId(session);
        String payload = message.getPayload();

        log.info("[STEP2] INBOUND frame: clientId={}, payload={}", clientId, payload);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String type = jsonNode.path("type").asText("client-message");
            String data = jsonNode.path("data").asText(payload);

            switch (type) {
                case "client-message" -> {
                    sessionService.sendToClient(clientId, "ack", "received type=" + type, "server");
                    sessionService.sendToClient(clientId, "message", data, "echo");
                }
                case "broadcast-request" -> {
                    int delivered = sessionService.broadcast("broadcast", data, "client:" + clientId);
                    sessionService.sendToClient(clientId, "broadcast-ack", "delivered=" + delivered, "server");
                }
                case "force-disconnect" -> {
                    sessionService.sendToClient(clientId, "disconnect-ack", "closing this session", "server");
                    sessionService.forceDisconnect(clientId);
                }
                default -> sessionService.sendToClient(clientId, "error", "unknown type=" + type, "server");
            }
        } catch (Exception ex) {
            sessionService.sendToClient(clientId, "error", "invalid JSON payload", "server");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String clientId = getClientId(session);
        sessionService.unregisterSession(clientId, "closeCode=" + status.getCode() + ", reason=" + status.getReason());
        log.info("[STEP3] connection closed clientId={}, status={}", clientId, status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        String clientId = getClientId(session);
        sessionService.unregisterSession(clientId, "transport error");
        log.warn("transport error for clientId={}", clientId, exception);
    }

    private String getClientId(WebSocketSession session) {
        Object value = session.getAttributes().get(ATTR_CLIENT_ID);
        return value == null ? "anonymous" : String.valueOf(value);
    }

    private String resolveClientId(URI uri) {
        if (uri == null || uri.getQuery() == null || uri.getQuery().isBlank()) {
            return "anonymous";
        }

        Map<String, String> params = parseQuery(uri.getQuery());
        return params.getOrDefault("clientId", "anonymous");
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        for (String part : query.split("&")) {
            String[] keyValue = part.split("=", 2);
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }
}
