package com.example.sse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SSE 엔드포인트 통합 테스트
 *
 * 각 단계별 동작을 검증합니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SseApplicationTest {

    @Autowired
    MockMvc mockMvc;

    // =========================================================================
    // [단계 1] SSE 연결 수립 테스트
    // =========================================================================

    @Test
    @DisplayName("[단계1] SSE 연결 - Content-Type이 text/event-stream이어야 한다")
    void connectShouldReturnEventStream() throws Exception {
        mockMvc.perform(get("/sse/connect/test-client")
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM));
    }

    @Test
    @DisplayName("[단계1] SSE 연결 응답에 connected 이벤트가 포함되어야 한다")
    void connectShouldSendConnectedEvent() throws Exception {
        MvcResult result = mockMvc.perform(get("/sse/connect/test-client-2")
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        // SSE 이벤트 구조 검증: "event: connected" 포함 여부
        assert body.contains("event:connected") : "응답에 'event:connected'가 없습니다: " + body;
        assert body.contains("id:") : "응답에 이벤트 ID(id:)가 없습니다";
    }

    @Test
    @DisplayName("[단계1] 재연결 시 Last-Event-ID 헤더를 수신해야 한다")
    void reconnectShouldHandleLastEventId() throws Exception {
        MvcResult result = mockMvc.perform(get("/sse/connect/test-reconnect")
                        .header("Last-Event-ID", "5")
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        // 재연결 시 lastEventId 정보가 응답에 포함되어야 함
        assert body.contains("reconnected") || body.contains("lastEventId") :
                "재연결 응답에 lastEventId 정보가 없습니다: " + body;
    }

    // =========================================================================
    // [단계 2] 정상 통신 테스트
    // =========================================================================

    @Test
    @DisplayName("[단계2] 미연결 클라이언트에게 메시지 전송 시 404 반환")
    void sendToNonExistentClientShouldReturn404() throws Exception {
        mockMvc.perform(post("/sse/send/non-existent-client")
                        .param("message", "Hello"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[단계2] 연결된 클라이언트에게 메시지 전송 성공")
    void sendMessageToConnectedClient() throws Exception {
        // 먼저 연결
        mockMvc.perform(get("/sse/connect/send-test-client")
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk());

        // 메시지 전송
        mockMvc.perform(post("/sse/send/send-test-client")
                        .param("message", "테스트 메시지"))
                .andExpect(status().isOk())
                .andExpect(content().string("전송 완료"));
    }

    @Test
    @DisplayName("[단계2] 브로드캐스트 성공")
    void broadcastShouldSucceed() throws Exception {
        // 클라이언트 2개 연결
        mockMvc.perform(get("/sse/connect/bc-client-1")
                .accept(MediaType.TEXT_EVENT_STREAM));
        mockMvc.perform(get("/sse/connect/bc-client-2")
                .accept(MediaType.TEXT_EVENT_STREAM));

        // 브로드캐스트
        mockMvc.perform(post("/sse/broadcast")
                        .param("message", "broadcast test"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("브로드캐스트 완료")));
    }

    // =========================================================================
    // [단계 3] 연결 종료 & 재연결 테스트
    // =========================================================================

    @Test
    @DisplayName("[단계3] 강제 종료 - 연결된 클라이언트 종료 성공")
    void disconnectConnectedClient() throws Exception {
        // 연결
        mockMvc.perform(get("/sse/connect/dc-test-client")
                .accept(MediaType.TEXT_EVENT_STREAM));

        // 강제 종료
        mockMvc.perform(post("/sse/disconnect/dc-test-client"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("연결 종료 완료")));
    }

    @Test
    @DisplayName("[단계3] 강제 종료 후 클라이언트 목록에서 제거되어야 한다")
    void disconnectShouldRemoveFromClientList() throws Exception {
        // 연결
        mockMvc.perform(get("/sse/connect/list-test-client")
                .accept(MediaType.TEXT_EVENT_STREAM));

        // 클라이언트 목록 확인 (연결됨)
        mockMvc.perform(get("/sse/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("list-test-client")));

        // 강제 종료
        mockMvc.perform(post("/sse/disconnect/list-test-client"));

        // 클라이언트 목록에서 제거 확인
        mockMvc.perform(get("/sse/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("list-test-client"))));
    }

    // =========================================================================
    // 유틸 엔드포인트 테스트
    // =========================================================================

    @Test
    @DisplayName("클라이언트 목록 조회 API")
    void getClientsShouldReturn200() throws Exception {
        mockMvc.perform(get("/sse/clients"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("홈 페이지 (Thymeleaf) - 200 OK 및 HTML 반환")
    void homeShouldReturnHtml() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
