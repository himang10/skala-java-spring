# WHAT: WebSocket 동작 이해와 현재 코드 흐름

## 1. 이 문서의 목적

이 문서는 다음을 한 번에 이해하기 위한 자료입니다.

1. WebSocket이 무엇인지
2. wss 환경에서 핸드셰이크가 어떻게 일어나는지
3. 핸드셰이크 이후 메시지 통신 구조가 어떤지
4. 현재 프로젝트 코드가 위 프로토콜 흐름을 어떻게 구현했는지

---

## 2. WebSocket이란 무엇인가

WebSocket은 클라이언트와 서버가 하나의 연결을 유지하면서 양방향으로 데이터를 주고받기 위한 프로토콜입니다.

핵심 특징:

- 최초 연결은 HTTP Upgrade 방식으로 시작
- 연결 성립 후에는 HTTP 요청/응답 반복이 아니라 프레임 기반 통신
- 서버도 필요할 때 즉시 클라이언트로 메시지 전송 가능

---

## 3. ws vs wss

- ws: 암호화되지 않은 WebSocket
- wss: TLS(HTTPS와 동일한 보안 채널) 위에서 동작하는 WebSocket

브라우저 코드에서 HTTPS 페이지라면 보통 wss로 연결해야 합니다.

현재 프로젝트도 아래 규칙으로 동작합니다.

- 페이지 프로토콜이 https면 wss 사용
- 페이지 프로토콜이 http면 ws 사용

구현 위치:

- src/main/resources/templates/index.html 의 wsUrl 함수

---

## 4. wss 핸드셰이크(Upgrade) 예시

핸드셰이크는 HTTP 형식이지만 TLS로 암호화된 채널 위에서 전달됩니다.

### 4.1 Client -> Server 요청 예시

```http
GET /ws/connect?clientId=user-001 HTTP/1.1
Host: example.com
Connection: Upgrade
Upgrade: websocket
Sec-WebSocket-Version: 13
Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==
Origin: https://example.com
```

설명:

- 이 요청은 "HTTP를 WebSocket으로 전환하자"는 요청
- 일반적으로 요청 바디는 없음

### 4.2 Server -> Client 응답 예시

```http
HTTP/1.1 101 Switching Protocols
Connection: Upgrade
Upgrade: websocket
Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
```

설명:

- 101 응답이 오면 업그레이드 성공
- 브라우저에서 onopen 이벤트가 호출됨

---

## 5. 핸드셰이크 후 메시지 통신 구조

업그레이드 이후에는 HTTP 헤더/바디가 아니라 WebSocket 프레임을 주고받습니다.

개념 구조:

1. Frame Header
- FIN, opcode(text/binary/close/ping/pong), payload 길이 등

2. Payload
- 텍스트(JSON) 또는 바이너리 데이터

현재 프로젝트는 텍스트(JSON) 페이로드를 사용합니다.

예시 payload (클라이언트 송신):

```json
{
  "type": "client-message",
  "clientId": "user-001",
  "data": "hello websocket",
  "sentAt": "2026-05-27T08:00:00.000Z"
}
```

예시 payload (서버 송신):

```json
{
  "sequence": 12,
  "eventType": "message",
  "clientId": "user-001",
  "data": "hello websocket",
  "source": "echo",
  "sentAt": "2026-05-27T08:00:00.123Z"
}
```

---

## 6. 전체 동작 흐름 (프로토콜 + 앱 동작)

1. 클라이언트에서 new WebSocket(url) 호출
2. 브라우저가 Upgrade 요청 전송
3. 서버가 101 응답
4. onopen 호출
5. 클라이언트 ws.send(...) 로 프레임 전송
6. 서버 handleTextMessage(...) 에서 프레임 수신
7. 서버가 ack/message/broadcast/... 프레임 응답
8. 클라이언트 onmessage 호출
9. 서버나 네트워크 이슈로 연결 종료 시 onclose 호출
10. 자동 재연결 로직으로 connectWs() 재호출

---

## 7. 현재 코드 기준 매핑

## 7.1 클라이언트(UI)

파일:
- src/main/resources/templates/index.html

주요 함수:

1. connectWs
- WebSocket 객체 생성
- onopen / onmessage / onclose / onerror 등록

2. sendClientMsg
- type=client-message 프레임 전송

3. sendBroadcastRequest
- type=broadcast-request 프레임 전송

4. requestForceDisconnect
- type=force-disconnect 프레임 전송

5. renderEvent
- 서버 수신 이벤트를 화면에 시각화

## 7.2 서버(WebSocket 엔드포인트 설정)

파일:
- src/main/java/com/example/websocket/config/WebSocketConfig.java

역할:

- /ws/connect 경로를 WebSocket 핸들러에 바인딩

## 7.3 서버(핸드셰이크 후 프레임 처리)

파일:
- src/main/java/com/example/websocket/handler/EduWebSocketHandler.java

역할:

1. afterConnectionEstablished
- clientId 추출
- 세션 등록
- connected 이벤트 전송

2. handleTextMessage
- 클라이언트 프레임 수신 및 type 분기
- client-message: ack + echo message 응답
- broadcast-request: 전체 브로드캐스트 + 요청자에게 ack
- force-disconnect: 종료 ack 후 세션 종료

3. afterConnectionClosed / handleTransportError
- 세션 정리

## 7.4 서버(세션/전송 관리)

파일:
- src/main/java/com/example/websocket/service/WebSocketSessionService.java

역할:

- 세션 저장/삭제
- 단일 전송(sendToClient)
- 브로드캐스트(broadcast)
- 강제 종료(forceDisconnect)
- 주기 heartbeat 전송
- connection-count 이벤트 전송

---

## 8. 이 프로젝트에서 중요한 이해 포인트

1. 업그레이드 전후를 구분해서 보기
- 전: HTTP Upgrade 요청/응답
- 후: WebSocket 프레임

2. 연결은 하나, 메시지는 다수
- 한 번 연결 후 여러 이벤트를 계속 송수신

3. 서버 발행 이벤트 수신 패널의 의미
- 서버가 보내는 이벤트를 sequence/eventType/source 기준으로 관찰
- 실제 프로토콜 흐름 학습용

4. 재연결은 클라이언트 책임
- onclose 이후 재연결 스케줄링 로직으로 복구

---

## 9. 빠른 점검 체크리스트

1. 연결 버튼 클릭 시 onopen 로그가 찍히는가
2. 일반 메시지 전송 시 ack/message 이벤트가 수신되는가
3. 브로드캐스트 요청 시 broadcast 이벤트가 수신되는가
4. 강제 종료 요청 시 onclose -> 자동 재연결이 동작하는가
5. connection-count 값이 접속/종료 시 변화하는가

---

## 10. 한 줄 정리

이 프로젝트는 "HTTP Upgrade로 연결을 열고, 이후는 WebSocket 프레임으로만 통신한다"는 원칙을 코드와 화면에서 그대로 보여주는 교육용 샘플입니다.
