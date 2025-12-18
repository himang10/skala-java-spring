# Spring Boot Async 교육 예제

Spring Boot의 `@Async` 어노테이션을 활용한 비동기 프로그래밍 예제입니다.

## 프로젝트 구조

```
09.async/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/skala/async/
│       │   ├── AsyncApplication.java          # 메인 애플리케이션 (@EnableAsync)
│       │   ├── config/
│       │   │   └── AsyncConfig.java           # ThreadPool 설정
│       │   ├── service/
│       │   │   └── AsyncService.java          # 비동기 서비스
│       │   └── controller/
│       │       └── AsyncController.java       # REST API 컨트롤러
│       └── resources/
│           └── application.properties
└── README.md
```

## 주요 개념

### 1. @EnableAsync
- Spring Boot 애플리케이션에서 비동기 처리를 활성화
- `AsyncApplication.java`에 설정

### 2. ThreadPoolTaskExecutor
- 비동기 작업을 처리할 스레드 풀 구성
- `AsyncConfig.java`에서 설정
  - CorePoolSize: 2 (기본 스레드 수)
  - MaxPoolSize: 5 (최대 스레드 수)
  - QueueCapacity: 100 (대기 큐 크기)

### 3. @Async
- 메서드를 비동기로 실행
- 반환 타입:
  - `void`: 결과 반환 없음
  - `CompletableFuture<T>`: 결과 반환

## API 엔드포인트

### 1. 비동기 메서드 (반환값 없음)
```bash
GET http://localhost:8080/api/async/void?message=test
```
- 즉시 응답 반환
- 백그라운드에서 비동기 작업 수행

### 2. 비동기 메서드 (반환값 있음)
```bash
GET http://localhost:8080/api/async/future?message=test
```
- CompletableFuture로 결과 반환
- 클라이언트는 결과를 기다림

### 3. 동기 메서드 (비교용)
```bash
GET http://localhost:8080/api/async/sync?message=test
```
- 작업이 완료될 때까지 대기
- 동기와 비동기의 차이를 비교

### 4. 여러 비동기 작업 병렬 처리
```bash
GET http://localhost:8080/api/async/multiple
```
- 3개의 비동기 작업을 병렬로 실행
- 모든 작업 완료 후 결과 조합하여 반환

## 실행 방법

### Maven으로 실행
```bash
cd 09.async
mvn spring-boot:run
```

### 빌드 및 실행
```bash
mvn clean package
java -jar target/springbootasyncsample-0.0.1-SNAPSHOT.jar
```

## 테스트 방법

### curl 사용
```bash
# 비동기 (void)
curl "http://localhost:8080/api/async/void?message=hello"

# 비동기 (future)
curl "http://localhost:8080/api/async/future?message=world"

# 동기
curl "http://localhost:8080/api/async/sync?message=sync-test"

# 병렬 처리
curl "http://localhost:8080/api/async/multiple"
```

### 로그 확인
실행 시 콘솔에서 다음을 확인할 수 있습니다:
- 각 메서드가 실행되는 스레드 이름
- 비동기 작업의 시작과 완료 시점
- 컨트롤러의 즉시 반환 동작

## 학습 포인트

1. **동기 vs 비동기**
   - `/sync` 엔드포인트는 작업 완료까지 기다림
   - `/void` 엔드포인트는 즉시 반환

2. **스레드 풀**
   - 로그에서 `async-1`, `async-2` 등의 스레드 이름 확인
   - 설정한 스레드 풀에서 작업 처리

3. **CompletableFuture**
   - 비동기 작업의 결과를 반환받을 수 있음
   - 여러 비동기 작업을 조합 가능

4. **병렬 처리**
   - 여러 작업을 동시에 실행하여 전체 처리 시간 단축
   - `CompletableFuture.allOf()` 활용

## 주의사항

1. `@Async`는 public 메서드에만 적용
2. 같은 클래스 내부에서 호출 시 프록시를 거치지 않아 비동기 동작 안 함
3. 예외 처리는 `AsyncUncaughtExceptionHandler` 구현 필요
