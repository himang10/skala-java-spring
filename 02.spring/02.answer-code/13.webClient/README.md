# WebClient Demo

## 프로젝트 설명
Spring WebClient를 사용하여 JSONPlaceholder API를 호출하는 간단한 데모 프로젝트입니다.

## 주요 기능
- **Mono 사용**: 단일 Post 조회 (ID 기반)
- **Flux 사용**: 여러 Posts 조회 후 첫 번째 1개만 반환
- 모든 응답은 Map으로 감싸서 반환 (1개의 Post만 포함)

## 기술 스택
- Spring Boot 3.1.5
- Spring WebFlux (WebClient)
- Lombok
- Java 17

## API 엔드포인트

### 1. Mono를 사용한 단일 Post 조회
```bash
GET http://localhost:8080/api/posts/mono/{id}
```

예시:
```bash
curl http://localhost:8080/api/posts/mono/1
```

응답:
```json
{
  "post": {
    "userId": 1,
    "id": 1,
    "title": "sunt aut facere repellat provident...",
    "body": "quia et suscipit..."
  }
}
```

### 2. Flux를 사용한 Posts 조회 (첫 번째 1개)
```bash
GET http://localhost:8080/api/posts/flux
```

예시:
```bash
curl http://localhost:8080/api/posts/flux
```

응답:
```json
{
  "post": {
    "userId": 1,
    "id": 1,
    "title": "sunt aut facere repellat provident...",
    "body": "quia et suscipit..."
  }
}
```

## 실행 방법

### Maven을 사용한 실행
```bash
./mvnw spring-boot:run
```

### 빌드 후 실행
```bash
./mvnw clean package
java -jar target/webclient-demo-1.0.0.jar
```

## 주요 클래스 설명

### 1. WebClientConfig
- WebClient Bean 설정
- 타임아웃 설정 (5초)
- Base URL 설정 (https://jsonplaceholder.typicode.com)

### 2. PostService
- `getPostByIdWithMono(Long id)`: Mono를 사용하여 ID로 Post 조회
- `getPostsWithFlux()`: Flux를 사용하여 Posts 조회 후 첫 번째 1개만 반환

### 3. PostController
- `/api/posts/mono/{id}`: Mono 방식 테스트 엔드포인트
- `/api/posts/flux`: Flux 방식 테스트 엔드포인트

## 설정 파일 (application.yaml)
```yaml
webclient:
  base-url: https://jsonplaceholder.typicode.com
  timeout: 5000
```

## 참고사항
- JSONPlaceholder API는 무료 테스트용 REST API입니다
- 실제 데이터베이스가 없어도 테스트 가능합니다
- Reactive 방식으로 비동기 처리됩니다
