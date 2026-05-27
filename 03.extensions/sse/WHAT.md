# WHAT: SSE 동작 이해와 현재 코드 흐름

## 1. 이 문서의 목적

이 문서는 다음을 한 번에 이해하기 위한 자료입니다.

1. SSE(Server-Sent Events)가 무엇인지
2. 브라우저가 SSE 연결을 어떻게 여는지
3. 이벤트 스트림이 어떤 형식으로 전달되는지
4. 현재 프로젝트 코드가 이 흐름을 어떻게 구현했는지

---

## 2. SSE란 무엇인가

SSE는 서버가 클라이언트로 이벤트를 지속적으로 밀어주는 단방향 스트리밍 방식입니다.

핵심 특징:

- 최초 연결은 일반 HTTP 요청으로 시작
- 응답은 `text/event-stream` 형식으로 유지
- 서버가 연결을 닫지 않는 한 스트림이 계속 열린 상태로 유지
- 브라우저는 연결이 끊기면 자동 재연결을 시도할 수 있음

---

## 3. SSE의 핵심 개념

SSE는 WebSocket처럼 양방향 프레임 통신이 아니라, 서버 → 클라이언트 방향의 이벤트 스트림입니다.

브라우저는 보통 `EventSource` API를 사용합니다.

현재 프로젝트도 아래 규칙으로 동작합니다.

- `new EventSource(url)` 로 연결 생성
- 서버는 `Content-Type: text/event-stream` 응답
- 이벤트는 `id`, `event`, `data` 필드 조합으로 전달
- 연결이 끊기면 `Last-Event-ID` 를 이용해 재연결

구현 위치:

- `src/main/resources/templates/index.html` 의 `connectSSE` 함수

---

## 4. SSE 연결 예시

SSE 연결은 HTTP 요청/응답으로 시작합니다.

### 4.1 Client -> Server 요청 예시

```http
GET /sse/connect/user-001 HTTP/1.1
Accept: text/event-stream
Cache-Control: no-cache
Last-Event-ID: 5
```

설명:

- 이 요청은 서버에 이벤트 스트림을 열어 달라는 요청
- 처음 연결할 때는 `Last-Event-ID` 가 없을 수 있음
- 재연결 시에는 마지막으로 받은 이벤트 ID가 포함될 수 있음

### 4.2 Server -> Client 응답 예시

```http
HTTP/1.1 200 OK
Content-Type: text/event-stream
Cache-Control: no-cache
Connection: keep-alive
```

설명:

- `200 OK` 로 응답하더라도 스트림은 계속 유지될 수 있음
- 응답 바디는 한 번에 끝나는 JSON이 아니라 이벤트 스트림

---

## 5. SSE 이벤트 포맷

SSE 이벤트는 아래처럼 텍스트 라인 형식으로 전달됩니다.

```text
id: 7
event: message
data: {"type":"message","data":"Hello"}
```

설명:

- `id` 는 재연결 기준이 되는 이벤트 식별자
- `event` 는 이벤트 타입
- `data` 는 클라이언트가 받는 실제 본문
- 빈 줄이 하나의 이벤트 종료를 의미함

여러 줄 데이터도 가능합니다.

```text
id: 8
event: message
data: line 1
data: line 2
```

---

## 6. 전체 동작 흐름

1. 클라이언트에서 `new EventSource(url)` 호출
2. 브라우저가 HTTP 요청 전송
3. 서버가 `text/event-stream` 응답
4. 스트림이 열린 상태에서 이벤트를 계속 전송
5. 브라우저의 `onmessage` 또는 `addEventListener` 가 이벤트를 수신
6. 서버가 스트림을 종료하거나 네트워크 문제가 생기면 연결이 끊김
7. 브라우저가 자동 재연결을 시도
8. 재연결 시 `Last-Event-ID` 를 함께 전달할 수 있음
9. 서버는 마지막 ID 이후 이벤트를 이어서 보낼 수 있음

---

## 7. 현재 코드 기준 매핑

## 7.1 클라이언트(UI)

파일:

- `src/main/resources/templates/index.html`

주요 함수:

1. `connectSSE`
- `EventSource` 객체 생성
- `connected`, `message`, `heartbeat`, `error` 이벤트 핸들러 등록

2. `disconnectSSE`
- 브라우저에서 연결을 수동 종료

3. `sendMessage`
- 특정 클라이언트에게 메시지 전송 요청

4. `broadcast`
- 연결된 모든 클라이언트에 브로드캐스트 요청

5. `forceDisconnect`
- 서버 쪽 연결 종료를 요청해서 재연결 흐름을 관찰

6. `appendEventBlock`
- 수신 이벤트를 화면에 구조화해서 표시

## 7.2 서버(SSE 엔드포인트 설정)

파일:

- `src/main/java/com/example/sse/controller/...`

역할:

- `/sse/connect/{clientId}` 경로로 SSE 스트림 제공
- 메시지 전송, 브로드캐스트, 강제 종료 요청 처리

## 7.3 서버(이벤트 송신)

파일:

- `src/main/java/com/example/sse/service/...`

역할:

- 클라이언트별 emitter 관리
- 단일 전송
- 브로드캐스트
- heartbeat 또는 상태 이벤트 전송
- 재연결 이후 이어보낼 이벤트 관리

---

## 8. 이 프로젝트에서 중요한 이해 포인트

1. 연결은 HTTP로 시작하지만, 유지되는 동안은 스트리밍 채널처럼 동작합니다.
2. 서버가 이벤트를 보내는 단방향 구조이므로, 클라이언트는 주로 수신과 재연결을 담당합니다.
3. `Last-Event-ID` 는 재연결에서 매우 중요합니다.
4. 화면의 이벤트 스트림 패널은 서버가 보낸 원문 구조를 학습용으로 보여줍니다.

---

## 9. 빠른 점검 체크리스트

1. 연결 버튼 클릭 시 SSE 스트림이 열리는가
2. connected 이벤트가 수신되는가
3. 메시지 전송 시 클라이언트별 수신 로그가 쌓이는가
4. 브로드캐스트 시 연결된 모든 클라이언트에 이벤트가 전달되는가
5. 강제 종료 후 자동 재연결 흐름이 보이는가
6. Last-Event-ID 가 재연결 시 유지되는가

---

## 10. 한 줄 정리

이 프로젝트는 "HTTP 스트림을 열고, 서버가 SSE 이벤트를 계속 밀어주는 구조"를 코드와 화면에서 그대로 보여주는 교육용 샘플입니다.
