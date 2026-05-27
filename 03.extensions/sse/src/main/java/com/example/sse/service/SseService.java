package com.example.sse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SSE 서비스
 *
 * SSE 프로토콜 기본 구조:
 * ----------------------------------
 * id: <이벤트 ID>
 * event: <이벤트 타입>
 * data: <이벤트 데이터>
 *                       ← 빈 줄이 이벤트의 끝을 나타냄
 * ----------------------------------
 */
@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    // SSE 연결 타임아웃: 0L = 무한대 (클라이언트가 연결 유지)
    private static final long SSE_TIMEOUT = 0L;

    // 연결된 클라이언트 목록: clientId → SseEmitter
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 이벤트 ID 전역 카운터 (재연결 시 Last-Event-ID 기반 복구에 사용)
    private final AtomicLong eventIdCounter = new AtomicLong(0);

    // =========================================================================
    // [단계 1] SSE 연결 수립
    // =========================================================================

    /**
     * 클라이언트와 SSE 연결을 수립합니다.
     *
     * @param clientId    클라이언트 식별자
     * @param lastEventId 재연결 시 브라우저가 보내는 마지막 이벤트 ID (최초 연결 시 null)
     * @return SseEmitter (Spring이 이 객체를 통해 이벤트를 스트리밍)
     */
    public SseEmitter connect(String clientId, String lastEventId) {
        // 기존 연결이 있으면 먼저 정리
        SseEmitter existing = emitters.get(clientId);
        if (existing != null) {
            log.info("[단계1] 기존 연결 정리: clientId={}", clientId);
            existing.complete();
            emitters.remove(clientId);
        }

        // SseEmitter 생성
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitters.put(clientId, emitter);

        // 연결 종료 콜백 (정상 종료)
        emitter.onCompletion(() -> {
            log.info("[단계1][연결 종료] 정상 종료: clientId={}, 잔여 연결={}", clientId, emitters.size() - 1);
            emitters.remove(clientId);
        });

        // 타임아웃 콜백
        emitter.onTimeout(() -> {
            log.warn("[단계1][연결 종료] 타임아웃 발생: clientId={}", clientId);
            emitters.remove(clientId);
        });

        // 에러 콜백
        emitter.onError(ex -> {
            log.error("[단계1][연결 종료] 에러 발생: clientId={}, error={}", clientId, ex.getMessage());
            emitters.remove(clientId);
        });

        // 초기 이벤트 전송: 연결 확인
        try {
            long eventId = eventIdCounter.incrementAndGet();
            boolean isReconnect = (lastEventId != null);

            printSseEventLog("connected",
                    eventId,
                    isReconnect ? "재연결 (Last-Event-ID=" + lastEventId + ")" : "최초 연결",
                    isReconnect ? "재연결" : "최초 연결");

            emitter.send(SseEmitter.event()
                    .id(String.valueOf(eventId))
                    .name("connected")
                    .data(isReconnect
                            ? "{\"type\":\"reconnected\",\"lastEventId\":" + lastEventId + "}"
                            : "{\"type\":\"connected\",\"clientId\":\"" + clientId + "\"}"));

            log.info("[단계1][연결 완료] clientId={}, 전체 연결 수={}", clientId, emitters.size());

        } catch (IOException e) {
            log.error("[단계1][초기 이벤트 전송 실패] clientId={}", clientId, e);
            emitters.remove(clientId);
        }

        return emitter;
    }

    // =========================================================================
    // [단계 2] 연결 후 정상 통신
    // =========================================================================

    /**
     * 특정 클라이언트에게 메시지 전송
     */
    public boolean sendMessage(String clientId, String message) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter == null) {
            log.warn("[단계2][전송 실패] 클라이언트 없음: clientId={}", clientId);
            return false;
        }

        try {
            long eventId = eventIdCounter.incrementAndGet();
            printSseEventLog("message", eventId, message, "단방향 메시지 전송");

            emitter.send(SseEmitter.event()
                    .id(String.valueOf(eventId))
                    .name("message")
                    .data("{\"type\":\"message\",\"clientId\":\"" + clientId + "\",\"data\":\"" + message + "\",\"eventId\":" + eventId + "}"));

            log.info("[단계2][전송 완료] clientId={}, eventId={}", clientId, eventId);
            return true;

        } catch (IOException e) {
            log.error("[단계2][전송 실패] 연결 끊김: clientId={}", clientId, e);
            emitters.remove(clientId);
            return false;
        }
    }

    /**
     * 연결된 전체 클라이언트에게 메시지 브로드캐스트
     */
    public int broadcast(String message) {
        log.info("[단계2][브로드캐스트] 대상 클라이언트 수={}, message={}", emitters.size(), message);
        int successCount = 0;
        for (String clientId : Set.copyOf(emitters.keySet())) {
            if (sendMessage(clientId, message)) {
                successCount++;
            }
        }
        log.info("[단계2][브로드캐스트 완료] 성공={}", successCount);
        return successCount;
    }

    /**
     * Heartbeat: 30초마다 연결 유지 이벤트 전송
     *
     * 왜 필요한가?
     * - 일부 proxy/방화벽은 일정 시간 트래픽이 없으면 연결을 끊음
     * - 주기적 ping으로 연결이 살아있음을 알림
     */
    @Scheduled(fixedDelay = 30_000)
    public void sendHeartbeat() {
        if (emitters.isEmpty()) return;

        log.debug("[단계2][Heartbeat] 전송 시작, 연결 수={}", emitters.size());
        for (String clientId : Set.copyOf(emitters.keySet())) {
            SseEmitter emitter = emitters.get(clientId);
            if (emitter == null) continue;
            try {
                long eventId = eventIdCounter.incrementAndGet();
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(eventId))
                        .name("heartbeat")
                        .data("{\"type\":\"heartbeat\",\"eventId\":" + eventId + "}"));
                log.debug("[단계2][Heartbeat] 전송 완료: clientId={}, eventId={}", clientId, eventId);
            } catch (IOException e) {
                log.warn("[단계2][Heartbeat 실패] 연결 제거: clientId={}", clientId);
                emitters.remove(clientId);
            }
        }
    }

    // =========================================================================
    // [단계 3] 연결 끊김 & 재연결
    // =========================================================================

    /**
     * 클라이언트 연결을 강제 종료합니다 (재연결 테스트용).
     *
     * 브라우저 EventSource 재연결 동작:
     * 1. 서버가 emitter.complete() 호출 → HTTP 응답 종료
     * 2. 브라우저 EventSource가 연결 끊김 감지
     * 3. 약 3초 후 자동으로 /sse/connect/{clientId} 재요청
     * 4. 재요청 시 Last-Event-ID 헤더에 마지막 수신 이벤트 ID 포함
     */
    public void disconnect(String clientId) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter == null) {
            log.warn("[단계3][강제 종료] 클라이언트 없음: clientId={}", clientId);
            return;
        }

        log.info("┌─────────────────────────────────────────────────┐");
        log.info("│ [단계3] 연결 강제 종료 시작                         │");
        log.info("│   clientId  = {}                         │", clientId);
        log.info("│   동작 흐름:                                      │");
        log.info("│   1. 서버 → emitter.complete() 호출               │");
        log.info("│   2. HTTP 스트림 종료 (200 OK 완결)               │");
        log.info("│   3. 브라우저 EventSource 재연결 타이머 시작 (~3s) │");
        log.info("│   4. GET /sse/connect/{clientId}  재요청          │");
        log.info("│   5. Last-Event-ID 헤더로 마지막 이벤트 ID 전달   │");
        log.info("└─────────────────────────────────────────────────┘");

        emitter.complete();
        emitters.remove(clientId);
    }

    /**
     * 현재 연결된 클라이언트 ID 목록 반환
     */
    public Set<String> getConnectedClients() {
        return emitters.keySet();
    }

    // =========================================================================
    // 내부 유틸: SSE 이벤트 구조를 로그로 명확히 표시
    // =========================================================================
    private void printSseEventLog(String eventType, long eventId, String data, String description) {
        log.info("┌─────────────────────────────────────────────────┐");
        log.info("│ SSE 이벤트 전송: {}                    │", description);
        log.info("├─────────────────────────────────────────────────┤");
        log.info("│  id: {}                                          │", eventId);
        log.info("│  event: {}                                       │", eventType);
        log.info("│  data: {}                                        │", data);
        log.info("│  <빈 줄> ← 이벤트 종료 구분자                    │");
        log.info("└─────────────────────────────────────────────────┘");
    }
}
