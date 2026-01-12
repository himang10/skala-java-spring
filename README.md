# SKALA Java & Spring Boot Training

이 프로젝트는 SKALA Java와 Spring Boot 교육을 위한 샘플 애플리케이션입니다.
This project is a sample application for SKALA Java and Spring Boot training.

## 프로젝트 개요 (Project Overview)

간단한 상품 관리 REST API를 통해 Spring Boot의 핵심 개념을 학습합니다.
Learn core Spring Boot concepts through a simple Product Management REST API.

### 주요 학습 내용 (Key Learning Topics)

- **Spring Boot 기본 구조** (Spring Boot Basics)
  - Auto-configuration
  - Component scanning
  - Dependency injection

- **계층별 아키텍처** (Layered Architecture)
  - Controller Layer (REST API)
  - Service Layer (Business Logic)
  - Repository Layer (Data Access)
  - Model/Entity Layer

- **Spring Data JPA**
  - Entity 정의 및 관계 매핑
  - Repository 인터페이스
  - Query methods
  - JPQL queries

- **REST API 설계** (REST API Design)
  - HTTP methods (GET, POST, PUT, DELETE)
  - Request/Response DTOs
  - Path variables and query parameters
  - HTTP status codes

- **예외 처리** (Exception Handling)
  - Custom exceptions
  - @RestControllerAdvice
  - Global exception handling

- **유효성 검사** (Validation)
  - Bean Validation (@Valid)
  - Custom validation messages

- **테스팅** (Testing)
  - Unit tests with Mockito
  - Integration tests with @WebMvcTest
  - MockMvc for controller testing

## 기술 스택 (Technology Stack)

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5 & Mockito

## 사전 요구사항 (Prerequisites)

- JDK 17 이상 (JDK 17 or higher)
- Maven 3.6 이상 (Maven 3.6 or higher)

## 설치 및 실행 (Installation & Running)

### 1. 프로젝트 클론 (Clone the project)

```bash
git clone https://github.com/himang10/skala-java-spring.git
cd skala-java-spring
```

### 2. 빌드 (Build)

```bash
mvn clean package
```

### 3. 실행 (Run)

```bash
mvn spring-boot:run
```

또는 (or):

```bash
java -jar target/spring-training-1.0.0.jar
```

애플리케이션은 http://localhost:8080 에서 실행됩니다.
The application will run at http://localhost:8080

## API 엔드포인트 (API Endpoints)

### 상품 관리 (Product Management)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | 모든 상품 조회 (Get all products) |
| GET | `/api/products/{id}` | 특정 상품 조회 (Get product by ID) |
| POST | `/api/products` | 새 상품 생성 (Create new product) |
| PUT | `/api/products/{id}` | 상품 수정 (Update product) |
| DELETE | `/api/products/{id}` | 상품 삭제 (Delete product) |
| GET | `/api/products/search?name={name}` | 이름으로 검색 (Search by name) |
| GET | `/api/products/filter/price?max={price}` | 가격으로 필터 (Filter by price) |
| GET | `/api/products/filter/stock?min={stock}` | 재고로 필터 (Filter by stock) |

### API 사용 예제 (API Usage Examples)

#### 1. 모든 상품 조회 (Get all products)

```bash
curl http://localhost:8080/api/products
```

#### 2. 상품 생성 (Create a product)

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Product",
    "description": "Product description",
    "price": 99.99,
    "stock": 50
  }'
```

#### 3. 상품 수정 (Update a product)

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Product",
    "description": "Updated description",
    "price": 149.99,
    "stock": 30
  }'
```

#### 4. 상품 삭제 (Delete a product)

```bash
curl -X DELETE http://localhost:8080/api/products/1
```

#### 5. 상품 검색 (Search products)

```bash
curl http://localhost:8080/api/products/search?name=laptop
```

## H2 콘솔 (H2 Console)

개발 중 데이터베이스를 확인하려면 H2 콘솔을 사용하세요.
To inspect the database during development, use the H2 console.

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:skaladb`
- Username: `sa`
- Password: (empty)

## 테스트 실행 (Running Tests)

### 모든 테스트 실행 (Run all tests)

```bash
mvn test
```

### 특정 테스트 클래스 실행 (Run specific test class)

```bash
mvn test -Dtest=ProductServiceTest
```

## 프로젝트 구조 (Project Structure)

```
src/
├── main/
│   ├── java/com/skala/training/
│   │   ├── SkalaSpringTrainingApplication.java  # Main application
│   │   ├── controller/                           # REST Controllers
│   │   │   └── ProductController.java
│   │   ├── service/                              # Business Logic
│   │   │   └── ProductService.java
│   │   ├── repository/                           # Data Access
│   │   │   └── ProductRepository.java
│   │   ├── model/                                # Domain Models
│   │   │   └── Product.java
│   │   ├── dto/                                  # Data Transfer Objects
│   │   │   ├── ProductRequest.java
│   │   │   └── ProductResponse.java
│   │   └── exception/                            # Exception Handling
│   │       ├── ResourceNotFoundException.java
│   │       ├── ErrorResponse.java
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       ├── application.properties                # Configuration
│       └── data.sql                             # Sample data
└── test/
    └── java/com/skala/training/
        ├── controller/
        │   └── ProductControllerTest.java       # Controller tests
        └── service/
            └── ProductServiceTest.java          # Service tests
```

## 학습 가이드 (Learning Guide)

### 1단계: 프로젝트 이해하기
- 전체 구조 파악
- 각 계층의 역할 이해
- 의존성 주입 패턴 학습

### 2단계: 엔티티와 리포지토리
- `Product.java` 엔티티 분석
- JPA 애노테이션 이해
- `ProductRepository.java` 쿼리 메서드 학습

### 3단계: 서비스 계층
- `ProductService.java` 비즈니스 로직 분석
- 트랜잭션 관리 이해
- DTO 변환 패턴 학습

### 4단계: 컨트롤러와 REST API
- `ProductController.java` API 설계 분석
- HTTP 메서드 매핑 이해
- 요청/응답 처리 학습

### 5단계: 예외 처리
- 커스텀 예외 생성
- 글로벌 예외 핸들러 이해
- 에러 응답 표준화

### 6단계: 테스트 작성
- 단위 테스트 작성법
- Mockito 사용법
- 통합 테스트 작성법

## 추가 학습 과제 (Additional Exercises)

1. **카테고리 기능 추가**
   - Category 엔티티 생성
   - Product와 Category 관계 설정 (ManyToOne)
   - 카테고리별 상품 조회 API 추가

2. **페이징 기능 구현**
   - Pageable 사용
   - 페이징된 상품 목록 API 구현

3. **검색 기능 확장**
   - 여러 조건으로 상품 검색
   - Specification 패턴 적용

4. **Spring Security 추가**
   - 인증/인가 구현
   - JWT 토큰 기반 보안

5. **API 문서화**
   - Swagger/OpenAPI 추가
   - API 문서 자동 생성

## 참고 자료 (References)

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

## 라이선스 (License)

이 프로젝트는 교육 목적으로 작성되었습니다.
This project is created for educational purposes.