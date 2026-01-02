# SessionEchoServer 구현 가이드

## Step 1: SimpleEchoServer 이해하기

### 핵심 개념
- **ServerSocket**: 클라이언트 연결 요청을 받는 서버 소켓 (포트 대기)
- **accept()**: 클라이언트 연결 요청 대기 (블로킹) → Socket 객체 반환
- **Socket**: 클라이언트와의 TCP 연결 (한 번의 통신만 처리 후 종료)

### SimpleEchoServer 흐름
```
1. ServerSocket(port) 생성
2. while(true) {
     - accept() : 클라이언트 대기
     - readLine() : 메시지 읽기 (블로킹)
     - println() : 응답 전송
     - close() : 연결 종료
   }
```

**특징**: 한 클라이언트 처리 후 종료 → 다음 클라이언트 대기

---

## Step 2: SessionEchoServer 구현하기

### 문제점: SimpleEchoServer는 한 번의 메시지만 처리
- 여러 메시지를 주고받으려면?
- 여러 클라이언트를 동시에 처리하려면?

### 해결책: Thread 활용

#### 단계 1: ClientHandler 클래스 정의
```java
static class ClientHandler implements Runnable {
    private Socket clientSocket;
    
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    
    @Override
    public void run() {
        // TODO: 클라이언트와 통신하는 로직 작성
    }
}
```

#### 단계 2: run() 메서드 구현
1. **스트림 생성**
   - `clientSocket.getInputStream()` → BufferedReader로 래핑
   - `clientSocket.getOutputStream()` → PrintWriter로 래핑

2. **메시지 루프** (SimpleEchoServer와의 차이)
   ```java
   while ((line = reader.readLine()) != null) {
       // 메시지 처리
       // "quit" 확인
       // 응답 전송
   }
   ```

3. **리소스 정리**
   - try-with-resources 사용
   - finally에서 소켓 close()

#### 단계 3: main() 메서드 수정
```java
// 각 클라이언트마다 새로운 스레드 생성
Thread clientHandler = new Thread(new ClientHandler(clientSocket));
clientHandler.start();  // 블로킹 없이 즉시 복귀
```

---

## Step 3: 핵심 개념 정리

### ServerSocket vs Socket
| 항목 | ServerSocket | Socket |
|------|-------------|--------|
| 역할 | 포트 대기 | 클라이언트와 통신 |
| 생성 | `new ServerSocket(port)` | `accept()` 반환 |
| 사용처 | 메인 스레드 | 워커 스레드 |

### Thread의 역할
- **Main Thread**: `accept()` → 계속 새 클라이언트 대기
- **Worker Thread**: 각 클라이언트와 메시지 송수신
- **이점**: 여러 클라이언트를 동시에 처리 가능

### start() vs run()
```java
clientHandler.start();  // ✅ 새 스레드 생성 (비동기)
clientHandler.run();    // ❌ 현재 스레드에서 실행 (블로킹)
```

---

## Step 4: 구현 체크리스트

- [ ] ClientHandler 클래스 정의 (implements Runnable)
- [ ] 생성자: Socket 객체 저장
- [ ] run() 메서드: try-with-resources로 스트림 생성
- [ ] while 루프: readLine() != null 조건
- [ ] "quit" 명령 처리
- [ ] echo 응답 전송
- [ ] main(): Thread 생성 및 start() 호출
- [ ] 테스트: 여러 클라이언트 동시 접속

---

## Step 5: 테스트 방법

```bash
# 터미널 1: 서버 실행
java SessionEchoServer

# 터미널 2, 3, 4: 클라이언트 접속 (동시 실행)
telnet localhost 8080
# 또는
nc localhost 8080
```

각 터미널에서 메시지 전송 → 모두 독립적으로 처리됨을 확인
