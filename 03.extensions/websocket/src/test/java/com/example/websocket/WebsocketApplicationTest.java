package com.example.websocket;

import com.example.websocket.service.WebSocketSessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebsocketApplicationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebSocketSessionService sessionService;

    @Test
    @DisplayName("home page should return HTML")
    void homeShouldReturnHtml() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    @DisplayName("missing session should return false when sending")
    void sendToMissingClientShouldReturnFalse() {
        boolean result = sessionService.sendToClient("missing-client", "message", "hello", "test");
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("registered session should receive frame")
    void sendToRegisteredClientShouldReturnTrue() throws Exception {
        WebSocketSession mockSession = Mockito.mock(WebSocketSession.class);
        when(mockSession.getId()).thenReturn("session-1");
        when(mockSession.isOpen()).thenReturn(true);

        sessionService.registerSession("test-client", mockSession);

        boolean result = sessionService.sendToClient("test-client", "message", "test-msg", "test");
        Assertions.assertTrue(result);

        verify(mockSession, atLeastOnce()).sendMessage(any(TextMessage.class));
        sessionService.unregisterSession("test-client", "test done");
    }

    @Test
    @DisplayName("forced disconnect should close session and return true")
    void forcedDisconnectShouldRemoveClient() throws Exception {
        WebSocketSession mockSession = Mockito.mock(WebSocketSession.class);
        when(mockSession.getId()).thenReturn("session-2");
        when(mockSession.isOpen()).thenReturn(true, true, false);

        sessionService.registerSession("disconnect-client", mockSession);

        boolean result = sessionService.forceDisconnect("disconnect-client");
        Assertions.assertTrue(result);

        verify(mockSession, atLeastOnce()).close(any());
    }

    @Test
    @DisplayName("broadcast should deliver to all open sessions")
    void broadcastShouldDeliverToOpenSessions() throws Exception {
        WebSocketSession first = Mockito.mock(WebSocketSession.class);
        WebSocketSession second = Mockito.mock(WebSocketSession.class);

        when(first.getId()).thenReturn("s-1");
        when(second.getId()).thenReturn("s-2");
        when(first.isOpen()).thenReturn(true);
        when(second.isOpen()).thenReturn(true);

        sessionService.registerSession("c-1", first);
        sessionService.registerSession("c-2", second);

        int delivered = sessionService.broadcast("broadcast", "hello all", "test");
        Assertions.assertEquals(2, delivered);

        verify(first, atLeastOnce()).sendMessage(any(TextMessage.class));
        verify(second, atLeastOnce()).sendMessage(any(TextMessage.class));

        sessionService.unregisterSession("c-1", "cleanup");
        sessionService.unregisterSession("c-2", "cleanup");
    }
}
