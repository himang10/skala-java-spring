# WebSocket 교육용 데모

이 샘플의 목적은 WebSocket의 핵심 동작을 다음 3단계로 직접 확인하는 것입니다.

1. 연결 수립(핸드셰이크)
2. 연결 후 정상 통신
3. 서버 측 연결 종료 후 재연결

코드는 의도적으로 단순하게 유지하여, 프로토콜 흐름을 눈으로 확인하고 이해하는 데 집중할 수 있도록 구성했습니다.

## 기술 스택

- Java 21
- Spring Boot 3.2.x
- Spring WebSocket + Spring MVC + Thymeleaf
- Maven, Gradle 모두 지원

## 이 샘플로 배우는 핵심

- WebSocket은 최초에 HTTP로 시작하지만, 연결 후에는 TCP 기반의 양방향 프레임 통신으로 전환됩니다.
- SSE와 달리 클라이언트도 서버로 자유롭게 메시지를 보낼 수 있습니다.
- 연결이 끊긴 뒤 재접속 전략은 브라우저/클라이언트 로직으로 구현해야 합니다.
- 이 샘플은 학습 혼동을 막기 위해 제어 흐름까지 WebSocket 프레임으로만 처리합니다.

## 전체 호출 흐름

1. 브라우저가 ws://host/ws/connect?clientId=... 로 연결 요청
2. 스프링이 핸드셰이크를 완료하고 HTTP 101 Switching Protocols 응답
3. 서버가 세션을 저장하고 connected 프레임 전송
4. 클라이언트가 JSON 텍스트 프레임 전송
5. 서버가 ack와 message 프레임으로 응답
6. 브로드캐스트 요청/강제 종료 요청도 WebSocket 프레임으로 전달
7. 서버가 세션을 종료하면 브라우저에서 close 이벤트 감지 후 3초 뒤 자동 재연결

## 단계별 코드 흐름

### 1단계: 연결 수립

- WebSocket 엔드포인트 등록
  - src/main/java/com/example/websocket/config/WebSocketConfig.java
- 핸드셰이크 완료 처리, clientId 추출, 세션 등록
  - src/main/java/com/example/websocket/handler/EduWebSocketHandler.java
  - src/main/java/com/example/websocket/service/WebSocketSessionService.java

이 단계에서 확인할 포인트:

- 상태 코드 101 전환 이후부터는 HTTP 요청/응답이 아니라 프레임 송수신으로 동작
- 서버 로그에 clientId, sessionId, 총 연결 수가 기록되는지 확인

### 2단계: 정상 통신

- 클라이언트 -> 서버 프레임 수신 및 파싱
  - src/main/java/com/example/websocket/handler/EduWebSocketHandler.java
- 서버 -> 클라이언트 응답/브로드캐스트
  - src/main/java/com/example/websocket/service/WebSocketSessionService.java
- 브라우저 실시간 프로토콜 로그
  - src/main/resources/templates/index.html

이 단계에서 확인할 포인트:

- 클라이언트에서 보낸 JSON이 서버에 정확히 도착하는지
- 서버가 보낸 응답 프레임에 sequence, eventType, clientId, source, data가 포함되는지
- 단일 전송과 브로드캐스트의 차이를 UI 로그로 구분할 수 있는지

### 3단계: 연결 종료 및 재연결

- 강제 종료 프레임(type=force-disconnect)
  - src/main/resources/templates/index.html
  - src/main/java/com/example/websocket/handler/EduWebSocketHandler.java
- 클라이언트 자동 재연결 로직
  - src/main/resources/templates/index.html

이 단계에서 확인할 포인트:

- 서버 종료 직후 close 이벤트가 발생하는지
- 약 3초 뒤 재연결 시도가 실행되는지
- 재연결 카운트가 증가하고 통신이 다시 정상 동작하는지

## 프레임 구조 이해

이 예제는 메시지를 다음과 같은 JSON 형태로 송수신합니다.

- sequence: 서버에서 부여한 증가 번호(메시지 순서 추적)
- eventType: 메시지 종류(connected, ack, message, broadcast 등)
- clientId: 대상/송신 클라이언트 식별자
- source: 메시지 생성 주체(server, echo, rest-api 등)
- data: 실제 비즈니스 데이터
- sentAt: 서버 전송 시각

sequence를 보면 메시지 순서를 추적할 수 있고,
eventType과 source를 함께 보면 메시지가 어떤 경로로 만들어졌는지 빠르게 파악할 수 있습니다.

## 설정

파일: src/main/resources/application.properties

- server.port=8081
- logging.level.com.example.websocket=DEBUG
- logging.level.org.springframework.web.socket=DEBUG

로그 레벨을 DEBUG로 둔 이유:

- 핸드셰이크와 프레임 흐름을 학습 목적으로 더 자세히 보기 위함

## 실행 방법

### Maven

1. websocket 디렉터리 이동
2. ./mvnw spring-boot:run

Maven Wrapper가 없다면:

- mvn spring-boot:run

### Gradle

1. websocket 디렉터리 이동
2. ./gradlew bootRun

Gradle Wrapper가 없다면:

- gradle bootRun

브라우저 접속:

- http://localhost:8081

## 실습 따라하기

1. Connect 클릭
2. 연결 로그에서 연결 성공과 세션 정보 확인
3. Client -> Server 전송으로 양방향 통신 확인
4. Server push, Broadcast로 메시지 경로 차이 확인
5. Force disconnect from server 실행
6. close 이벤트 발생 후 자동 재연결 로그 확인
7. 재연결 후 다시 메시지 전송하여 통신 복구 확인

권장 관찰 순서:

- 브라우저 로그(가시성)
- 서버 콘솔 로그(정확한 프로토콜 필드)
- connection-count 프레임(현재 연결 상태)

## 테스트

Maven:

- mvn test

Gradle:

- gradle test

테스트에서 검증하는 항목:

- 홈 페이지 렌더링
- 클라이언트 목록 조회
- 미연결 대상 전송 시 404
- 등록된 세션 전송 성공
- 강제 종료 후 세션 제거
