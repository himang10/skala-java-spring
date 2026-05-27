package com.example.sse.controller;

import com.example.sse.service.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;

/**
 * SSE REST 컨트롤러
 *
 * 엔드포인트 목록:
 * ┌──────────────────────────────────────────────────────────────────┐
 * │ [단계1] GET  /sse/connect/{clientId}   → SSE 연결 수립           │
 * │ [단계2] POST /sse/send/{clientId}      → 특정 클라이언트 메시지  │
 * │ [단계2] POST /sse/broadcast            → 전체 브로드캐스트       │
 * │ [단계3] POST /sse/disconnect/{clientId}→ 강제 종료(재연결 테스트)│
 * │         GET  /sse/clients              → 연결된 클라이언트 목록  │
 * └──────────────────────────────────────────────────────────────────┘
 */
@RestController
@RequestMapping("/sse")
public class SseController {

    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    // =========================================================================
    // [단계 1] SSE 연결 수립
    // =========================================================================

    /**
     * SSE 연결 엔드포인트
     *
     * HTTP 프로토콜 관점:
     * - 요청: GET /sse/connect/{clientId} (Accept: text/event-stream)
     * - 응답: 200 OK, Content-Type: text/event-stream
     * - 응답 바디: 무한 스트림 (연결이 끊길 때까지 이벤트를 계속 전송)
     *
     * @param clientId    클라이언트 ID (URL 경로 변수)
     * @param lastEventId 재연결 시 브라우저가 자동으로 보내는 헤더
     */
    @GetMapping(value = "/connect/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
            @PathVariable String clientId,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

        log.info("═══════════════════════════════════════════════════");
        log.info("[단계1] SSE 연결 요청 수신");
        log.info("  클라이언트 ID : {}", clientId);
        log.info("  Last-Event-ID : {} ({})", lastEventId,
                lastEventId != null ? "재연결" : "최초 연결");
        log.info("  응답 헤더 예시:");
        log.info("    Content-Type: text/event-stream");
        log.info("    Cache-Control: no-cache");
        log.info("    X-Accel-Buffering: no  (nginx proxy 통과 시 필요)");
        log.info("═══════════════════════════════════════════════════");

        return sseService.connect(clientId, lastEventId);
    }

    // =========================================================================
    // [단계 2] 정상 통신 - 메시지 전송
    // =========================================================================

    /**
     * 특정 클라이언트에게 메시지 전송
     */
    @PostMapping("/send/{clientId}")
    public ResponseEntity<String> send(
            @PathVariable String clientId,
            @RequestParam String message) {

        log.info("[단계2] 메시지 전송 요청: clientId={}, message={}", clientId, message);
        boolean success = sseService.sendMessage(clientId, message);

        return success
                ? ResponseEntity.ok("전송 완료")
                : ResponseEntity.notFound().build();
    }

    /**
     * 연결된 전체 클라이언트에게 메시지 브로드캐스트
     */
    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestParam String message) {
        log.info("[단계2] 브로드캐스트 요청: message={}", message);
        int count = sseService.broadcast(message);
        return ResponseEntity.ok("브로드캐스트 완료 (수신 클라이언트: " + count + "명)");
    }

    // =========================================================================
    // [단계 3] 연결 종료 → 재연결 테스트
    // =========================================================================

    /**
     * 특정 클라이언트 연결 강제 종료
     * 브라우저 EventSource는 연결이 끊기면 자동으로 재연결을 시도합니다.
     */
    @PostMapping("/disconnect/{clientId}")
    public ResponseEntity<String> disconnect(@PathVariable String clientId) {
        log.info("[단계3] 강제 종료 요청: clientId={}", clientId);
        sseService.disconnect(clientId);
        return ResponseEntity.ok("연결 종료 완료. 브라우저가 자동으로 재연결합니다.");
    }

    // =========================================================================
    // 유틸: 연결 상태 조회
    // =========================================================================

    @GetMapping("/clients")
    public ResponseEntity<Set<String>> getClients() {
        Set<String> clients = sseService.getConnectedClients();
        log.info("[상태 조회] 현재 연결 클라이언트: {}", clients);
        return ResponseEntity.ok(clients);
    }
}
