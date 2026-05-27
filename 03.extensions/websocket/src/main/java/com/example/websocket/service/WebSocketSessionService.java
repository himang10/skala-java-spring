package com.example.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WebSocketSessionService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketSessionService.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);
    private final ObjectMapper objectMapper;

    public WebSocketSessionService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerSession(String clientId, WebSocketSession session) {
        WebSocketSession existing = sessions.get(clientId);
        if (existing != null && existing.isOpen()) {
            closeQuietly(existing, CloseStatus.NORMAL.withReason("replaced by reconnect"));
        }

        sessions.put(clientId, session);

        log.info("====================================================");
        log.info("[STEP1] WebSocket CONNECTED");
        log.info("  clientId   : {}", clientId);
        log.info("  sessionId  : {}", session.getId());
        log.info("  total open : {}", sessions.size());
        log.info("====================================================");

        publishConnectionCount();
    }

    public void unregisterSession(String clientId, String reason) {
        sessions.remove(clientId);
        log.info("[STEP3] session removed clientId={}, reason={}, remaining={}", clientId, reason, sessions.size());
        publishConnectionCount();
    }

    public boolean sendToClient(String clientId, String eventType, String data, String source) {
        WebSocketSession session = sessions.get(clientId);
        if (session == null || !session.isOpen()) {
            log.warn("[STEP2] send failed: missing or closed session for clientId={}", clientId);
            return false;
        }

        long seq = sequence.incrementAndGet();
        MessageEnvelope payload = new MessageEnvelope(seq, eventType, clientId, data, source, Instant.now().toString());
        String json = toJson(payload);

        try {
            session.sendMessage(new TextMessage(json));
            printFrameLog("OUTBOUND", payload);
            return true;
        } catch (IOException ex) {
            log.error("[STEP2] send failed due to IO error for clientId={}", clientId, ex);
            sessions.remove(clientId);
            return false;
        }
    }

    public int broadcast(String eventType, String data, String source) {
        int success = 0;
        for (String clientId : Set.copyOf(sessions.keySet())) {
            if (sendToClient(clientId, eventType, data, source)) {
                success++;
            }
        }
        log.info("[STEP2] broadcast done, success={}", success);
        return success;
    }

    public boolean forceDisconnect(String clientId) {
        WebSocketSession session = sessions.get(clientId);
        if (session == null) {
            return false;
        }

        closeQuietly(session, CloseStatus.SERVICE_RESTARTED.withReason("forced by server"));
        sessions.remove(clientId);

        log.info("[STEP3] forced disconnect done, clientId={}", clientId);
        publishConnectionCount();
        return true;
    }

    public Set<String> getConnectedClients() {
        return new TreeSet<>(sessions.keySet());
    }

    @Scheduled(fixedDelay = 30_000)
    public void heartbeat() {
        if (sessions.isEmpty()) {
            return;
        }
        broadcast("heartbeat", "ping", "server-heartbeat");
    }

    private String toJson(MessageEnvelope payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("failed to serialize message", ex);
        }
    }

    private void closeQuietly(WebSocketSession session, CloseStatus closeStatus) {
        try {
            if (session.isOpen()) {
                session.close(closeStatus);
            }
        } catch (IOException ex) {
            log.debug("failed to close session quietly", ex);
        }
    }

    private void printFrameLog(String direction, MessageEnvelope payload) {
        log.info("+--------------------------------------------------+");
        log.info("| WS FRAME {}", direction);
        log.info("| seq       : {}", payload.sequence());
        log.info("| type      : {}", payload.eventType());
        log.info("| clientId  : {}", payload.clientId());
        log.info("| source    : {}", payload.source());
        log.info("| data      : {}", payload.data());
        log.info("+--------------------------------------------------+");
    }

    private void publishConnectionCount() {
        if (sessions.isEmpty()) {
            return;
        }
        broadcast("connection-count", String.valueOf(sessions.size()), "server");
    }

    public record MessageEnvelope(
            long sequence,
            String eventType,
            String clientId,
            String data,
            String source,
            String sentAt
    ) {}
}
