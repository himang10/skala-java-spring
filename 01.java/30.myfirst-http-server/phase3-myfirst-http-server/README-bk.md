---
marp: true
theme: default
paginate: true
size: 16:9
backgroundColor: #ffffff
---

<style>
section {
  background: #ffffff;
  color: #1e293b;
  font-family: 'Noto Sans KR', 'Malgun Gothic', 'Apple SD Gothic Neo', sans-serif;
  font-size: 22px;
  padding: 50px 80px;
}
h1 {
  color: #0f172a;
  font-size: 2.5rem;
  font-weight: 700;
  border-bottom: 3px solid #3b82f6;
  padding-bottom: 0.5rem;
  margin-bottom: 1.5rem;
}
h2 {
  color: #1e40af;
  font-size: 1.8rem;
  font-weight: 600;
  margin-top: 1.5rem;
  margin-bottom: 1rem;
}
h3 {
  color: #1e40af;
  font-size: 1.4rem;
  font-weight: 600;
  margin-top: 1rem;
  margin-bottom: 0.8rem;
}
code {
  background: #f1f5f9;
  color: #0f172a;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
  font-family: 'D2Coding', 'Consolas', 'Monaco', monospace;
  font-size: 1.0em;
}
pre {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-left: 4px solid #3b82f6;
  border-radius: 6px;
  font-family: 'D2Coding', 'Consolas', 'Monaco', monospace;
  font-size: 0.75em;
  line-height: 1.6;
  padding: 1.2rem;
  margin: 1rem 0;
  margin-bottom: 40px;
  max-height: 65vh;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}
pre code {
  background: transparent;
  padding: 0;
  color: #1e293b;
  font-size: 0.75em;
}
ul, ol {
  margin-left: 1.5rem;
  line-height: 1.8;
}
li {
  margin: 0.5rem 0;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin: 1.5rem 0;
  font-size: 0.95em;
}
th, td {
  border: 1px solid #cbd5e1;
  padding: 0.75rem;
  text-align: left;
}
th {
  background: #f1f5f9;
  color: #0f172a;
  font-weight: 600;
}
blockquote {
  border-left: 4px solid #3b82f6;
  padding-left: 1rem;
  margin: 1rem 0;
  color: #475569;
  font-style: italic;
}
section.lead {
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
section.lead h1 {
  border-bottom: none;
  font-size: 3rem;
  margin-bottom: 1rem;
}
section:not(.lead) {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}
</style>

<!-- _class: lead -->
<!-- _paginate: false -->
# My First HTTP Server - Pure Java Socket Implementation

## 프로젝트 개요

이 프로젝트는 **Java Socket을 이용하여 HTTP 서버를 직접 구현한 교육용 프로젝트**입니다. Spring Boot와 같은 프레임워크 없이 순수 Java만으로 HTTP 요청을 처리하는 과정을 학습할 수 있습니다.

### 핵심 컨셉

- **TCP/IP 소켓 통신**: `ServerSocket`과 `Socket`을 이용하여 네트워크 통신 계층부터 직접 구현
- **HTTP 프로토콜 파싱**: TCP/IP를 통해 수신되는 바이트 스트림을 HTTP 헤더와 페이로드로 파싱
- **MVC 패턴 에뮬레이션**: Spring Boot의 Controller, Service, Repository 구조를 모방하여 RESTful API 구현
- **라우팅 시스템**: URL 패턴 매칭과 경로 변수 추출 기능을 갖춘 Router 구현
- **멀티스레드 처리**: Thread Pool을 이용한 동시 다중 클라이언트 요청 처리

## 주요 Java 네트워크 클래스

### 1. ServerSocket

```java
ServerSocket serverSocket = new ServerSocket(8080);
Socket clientSocket = serverSocket.accept(); // 클라이언트 연결 대기
```

- **역할**: 서버 측에서 클라이언트 연결 요청을 수신(listen)하는 소켓
- **주요 기능**:
  - 특정 포트에서 연결 대기 (listening)
  - `accept()` 메서드로 클라이언트 연결 수락 시 `Socket` 객체 반환
  - 연결이 올 때까지 블로킹(blocking)되어 대기
- **특징**: 실제 데이터 송수신은 하지 않고, 오직 연결 수락만 담당

### 2. Socket

```java
Socket clientSocket = serverSocket.accept();
InputStream input = clientSocket.getInputStream();
OutputStream output = clientSocket.getOutputStream();
```

- **역할**: 클라이언트와 서버 간 실제 데이터 송수신을 담당하는 양방향 통신 채널
- **주요 기능**:
  - `getInputStream()`: 클라이언트로부터 데이터 수신
  - `getOutputStream()`: 클라이언트에게 데이터 전송
  - `getInetAddress()`: 연결된 클라이언트 IP 정보 조회
- **특징**: TCP/IP 연결이 수립된 후 실제 바이트 스트림을 주고받는 통로

### 3. InetAddress

```java
InetAddress address = clientSocket.getInetAddress();
String clientIp = address.getHostAddress(); // "192.168.1.100"
String hostname = address.getHostName();    // "client-pc.local"
```

- **역할**: IP 주소와 호스트 정보를 표현하는 클래스
- **주요 기능**:
  - `getHostAddress()`: IP 주소를 문자열로 반환
  - `getHostName()`: 호스트명 반환 (역방향 DNS 조회)
  - `getLocalHost()`: 로컬 머신의 IP 정보 조회
- **특징**: IPv4와 IPv6를 모두 지원하며, DNS 조회 기능 포함

## 아키텍처 및 데이터 흐름

### 전체 구조

```
┌─────────────┐
│   Client    │ (curl, 브라우저, Postman 등)
└──────┬──────┘
       │ HTTP Request (TCP/IP Socket Stream)
       ↓
┌──────────────────────────────────────────┐
│        ServerSocket (Port 8080)          │
│  - accept() 로 클라이언트 연결 대기      │
└──────┬───────────────────────────────────┘
       │ Socket 객체 생성
       ↓
┌──────────────────────────────────────────┐
│    Thread Pool (ExecutorService)         │
│  - 각 CLI 요청을 별도 스레드에서 처리    │
└──────┬───────────────────────────────────┘
       │
       ↓
┌──────────────────────────────────────────┐
│       HttpServerLite.handleClient()      │
│  ① Socket Stream → HTTP Request 파싱     │
│  ② Router를 통한 핸들러 매칭             │
│  ③ RouteHandler 실행                     │
│  ④ HTTP Response 생성 및 전송            │
└──────┬───────────────────────────────────┘
       │
       ├─→ HttpRequest.parse()  : 바이트 스트림 → HTTP 객체
       ├─→ Router.match()       : URL 패턴 매칭 + 경로 변수 추출
       ├─→ RouteHandler.handle(): 비즈니스 로직 실행 (CRUD)
       └─→ HttpResponse.write() : HTTP 응답 생성 및 전송
```

### 상세 데이터 처리 흐름

#### 1단계: 서버 시작 및 연결 대기 (Main.java)

```java
ServerSocket serverSocket = new ServerSocket(8080);
while (true) {
    Socket clientSocket = serverSocket.accept(); // 블로킹
    threadPool.submit(() -> handleClient(clientSocket));
}
```

- `ServerSocket`이 8080 포트에서 대기
- 클라이언트 연결 시 `Socket` 객체 생성
- Thread Pool에 작업 제출하여 비동기 처리

#### 2단계: HTTP 요청 파싱 (HttpRequest.parse)

```
클라이언트가 보낸 바이트 스트림:
─────────────────────────────────────
GET /users/123 HTTP/1.1\r\n
Host: localhost:8080\r\n
Content-Type: application/json\r\n
Content-Length: 45\r\n
\r\n
{"name":"홍길동","email":"hong@example.com"}
─────────────────────────────────────

↓ parse() 메서드 처리

HttpRequest 객체:
─────────────────────────────────────
method: "GET"
path: "/users/123"
headers: {
  "host": "localhost:8080",
  "content-type": "application/json",
  "content-length": "45"
}
body: "{\"name\":\"홍길동\",\"email\":\"hong@example.com\"}"
─────────────────────────────────────
```

**파싱 과정**:
1. `BufferedReader`로 첫 줄 읽기 → HTTP 메서드, 경로, 버전 추출
2. 빈 줄(`\r\n`)이 나올 때까지 헤더 파싱 → `Map<String,String>`에 저장
3. `Content-Length` 헤더 확인 → 바디 크기만큼 읽기
4. `HttpRequest` 객체에 모든 정보 담아 반환

#### 3단계: 라우터 매칭 (Router.match)

```java
// Router에 등록된 패턴들
router.add("GET", "/users", handler1);
router.add("GET", "/users/{id}", handler2);
router.add("POST", "/users", handler3);

// 들어온 요청: GET /users/123
var matched = router.match("GET", "/users/123");
// → handler2 선택
// → pathVars = {id: "123"}
```

**매칭 과정**:
1. URL 패턴을 정규표현식으로 변환
   - `/users/{id}` → `/users/([^/]+)`
2. 정규표현식으로 실제 경로와 매칭
3. 캡처 그룹에서 경로 변수 값 추출
4. 매칭된 핸들러와 경로 변수 반환

#### 4단계: 비즈니스 로직 실행 (RouteHandler)

```java
router.add("GET", "/users/{id}", (req, res) -> {
    Long id = req.pathLong("id"); // "123" → 123L
    User user = repository.findById(id);
    String json = JsonUtil.toJson(user);
    HttpResponse.writeJson(res, 200, json);
});
```

**실행 과정**:
1. 경로 변수에서 `id` 추출 및 타입 변환
2. Repository를 통해 데이터 조회 (파일 I/O)
3. Java 객체 → JSON 문자열 변환
4. HTTP 응답 생성 및 전송

#### 5단계: HTTP 응답 생성 (HttpResponse.write)

```
HTTP/1.1 200 OK\r\n
Content-Type: application/json; charset=utf-8\r\n
Content-Length: 87\r\n
Connection: close\r\n
\r\n
{
  "id": 123,
  "name": "홍길동",
  "email": "hong@example.com",
  "hobbies": ["독서", "운동"]
}
```

**응답 생성 과정**:
1. 상태 라인 작성: `HTTP/1.1 200 OK`
2. 헤더 작성: Content-Type, Content-Length, Connection
3. 빈 줄 추가: 헤더와 바디 구분
4. 바디 작성: JSON 데이터
5. `OutputStream.write()`로 소켓에 전송

## 프로젝트 구조

```
src/main/java/com/skala/purejava/http/
├── Main.java                    # 애플리케이션 진입점
├── model/
│   └── User.java               # 사용자 도메인 모델
├── repo/
│   ├── UserRepository.java     # Repository 인터페이스
│   └── FileUserRepository.java # 파일 기반 저장소 구현
├── server/
│   ├── HttpServerLite.java     # 핵심 HTTP 서버 (Socket 처리)
│   ├── HttpRequest.java        # HTTP 요청 파싱 및 표현
│   ├── HttpResponse.java       # HTTP 응답 생성
│   ├── Router.java             # URL 라우팅 및 패턴 매칭
│   ├── RouteHandler.java       # 핸들러 인터페이스 (함수형)
│   └── HttpUserHandler.java    # User CRUD 라우트 정의
└── util/
    └── JsonUtil.java           # JSON 직렬화/역직렬화

data/
└── users.json                  # 사용자 데이터 저장 파일
```

## 주요 컴포넌트 설명

### HttpServerLite (핵심 서버 엔진)

**역할**: TCP/IP Socket 수준에서 HTTP 요청을 받아 처리하는 핵심 엔진

```java
public class HttpServerLite {
    private final int port;
    private final Router router;
    private final ExecutorService threadPool;
    
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            }
        }
    }
}
```

**핵심 기능**:
- `ServerSocket`으로 클라이언트 연결 수락
- Thread Pool을 이용한 멀티스레드 요청 처리
- 소켓 스트림 → HTTP 파싱 → 라우터 매칭 → 응답 전송 파이프라인

### Router (URL 라우팅 시스템)

**역할**: Spring의 `@RequestMapping`과 유사한 URL 패턴 매칭 기능

```java
Router router = new Router();
router.add("GET", "/users", handler);
router.add("GET", "/users/{id}", handler);
router.add("POST", "/users", handler);
```

**핵심 기능**:
- URL 패턴을 정규표현식으로 변환 (`/users/{id}` → `/users/([^/]+)`)
- 요청 URL과 패턴 매칭
- 경로 변수 추출 및 제공 (`{id}` → `pathVars.get("id")`)

### HttpRequest (요청 파싱)

**역할**: 소켓의 바이트 스트림을 구조화된 HTTP 요청 객체로 변환

```java
HttpRequest request = HttpRequest.parse(inputStream);
// request.method → "GET"
// request.path → "/users/123"
// request.headers → {"host": "localhost:8080", ...}
// request.body → "{...}"
```

**파싱 요소**:
- Request Line: 메서드, 경로, 쿼리 스트링
- Headers: key-value 맵으로 저장
- Body: `Content-Length` 기반으로 읽기

### HttpResponse (응답 생성)

**역할**: Java 객체를 HTTP 응답 형식으로 직렬화하여 전송

```java
HttpResponse.writeJson(outputStream, 200, jsonString);
HttpResponse.writeText(outputStream, 204, "");
```

**응답 구성**:
- 상태 라인: `HTTP/1.1 200 OK`
- 필수 헤더: Content-Type, Content-Length, Connection
- 바디: JSON 또는 텍스트 데이터

### HttpUserHandler (CRUD 라우트 정의)

**역할**: Spring Controller와 유사하게 RESTful API 엔드포인트 정의

```java
public class HttpUserHandler extends HttpServerLite {
    // 생성자에서 Router 설정 및 서버 초기화
    public HttpUserHandler(int port, int threads, UserRepository repo) {
        super(port, buildRouter(repo), threads);
    }
}
```

**지원 API**:
- `GET /users` - 전체 사용자 조회
- `GET /users/{id}` - 특정 사용자 조회
- `POST /users` - 사용자 생성
- `PUT /users/{id}` - 사용자 수정
- `DELETE /users/{id}` - 사용자 삭제

## 빌드 및 실행

### 요구사항

- Java 17 이상
- Maven 3.6 이상

### 빌드

```bash
# Maven을 이용한 빌드
./build.sh

# 또는
mvn clean package
```

### 실행

```bash
# 실행 스크립트 사용
./run.sh

# 또는 직접 실행
java -cp target/classes:target/dependency/* com.skala.purejava.http.Main
```

서버는 `http://localhost:8080`에서 실행됩니다.

## API 사용 예제

### 전체 사용자 조회

```bash
curl http://localhost:8080/users
```

**응답**:
```json
[
  {
    "id": 1,
    "name": "홍길동",
    "email": "hong@example.com",
    "hobbies": ["독서", "운동"]
  }
]
```

### 특정 사용자 조회

```bash
curl http://localhost:8080/users/1
```

### 사용자 생성

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "김철수",
    "email": "kim@example.com",
    "hobbies": ["영화", "음악"]
  }'
```

### 사용자 수정

```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "email": "hong.updated@example.com",
    "hobbies": ["독서", "운동", "여행"]
  }'
```

### 사용자 삭제

```bash
curl -X DELETE http://localhost:8080/users/1
```

## 학습 포인트

### 1. 네트워크 프로그래밍 기초
- TCP/IP 소켓 통신의 이해
- `ServerSocket`과 `Socket`의 역할 분리
- InputStream/OutputStream을 통한 바이트 스트림 처리

### 2. HTTP 프로토콜 이해
- HTTP 요청/응답 구조 (Request Line, Headers, Body)
- HTTP 메서드와 상태 코드의 의미
- Content-Type, Content-Length 등 헤더의 역할

### 3. 멀티스레드 프로그래밍
- `ExecutorService`를 이용한 Thread Pool 관리
- 동시 다중 요청 처리 방식
- 스레드 안전성 고려사항

### 4. 디자인 패턴
- MVC 패턴: Model(User) - View(JSON) - Controller(Handler)
- Repository 패턴: 데이터 접근 계층 추상화
- Strategy 패턴: RouteHandler 인터페이스

### 5. 프레임워크 동작 원리
- Spring Boot의 `@RestController`, `@RequestMapping` 동작 방식
- 라우팅과 경로 변수 처리 메커니즘
- Request/Response 변환 과정

## 제한사항 및 개선 가능 사항

### 현재 제한사항
- HTTP/1.0 수준의 단순 구현 (Keep-Alive, Chunked Transfer 미지원)
- HTTPS/SSL 미지원
- 정적 파일 서빙 미구현
- 멀티파트 폼 데이터 미지원
- 에러 핸들링 단순화

### 개선 가능 사항
- HTTP/1.1 완전 구현 (persistent connection)
- NIO 기반 비동기 I/O로 성능 개선
- 미들웨어/인터셉터 패턴 추가
- 요청/응답 검증 계층 추가
- 로깅 및 모니터링 강화

## 기술 스택

- **Java 17**: 핵심 언어
- **Socket API**: 네트워크 통신
- **Jackson 2.17.2**: JSON 직렬화/역직렬화
- **ExecutorService**: 멀티스레드 처리
- **Maven**: 빌드 도구

## 라이선스

교육용 프로젝트

---

**참고**: 이 프로젝트는 교육 목적으로 제작되었으며, 실제 프로덕션 환경에서는 검증된 프레임워크(Spring Boot, Netty 등)를 사용하는 것을 권장합니다.
