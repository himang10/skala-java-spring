# Spring Boot AOP Annotation 교육 예제

@Hint 커스텀 어노테이션을 기반으로 RUNTIME에 실행되는 AOP Proxy를 처리하는 샘플입니다.

## 프로젝트 구조

```
08.aop-annotation/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/skala/aopanotation/
│       │   ├── AopAnnotationApplication.java      # 메인 애플리케이션
│       │   ├── annotation/
│       │   │   └── Hint.java                      # @Hint 커스텀 어노테이션
│       │   ├── aspect/
│       │   │   └── HintAspect.java                # @Hint를 처리하는 Aspect
│       │   ├── service/
│       │   │   └── HintService.java               # @Hint 적용된 서비스
│       │   └── controller/
│       │       └── HintController.java            # REST API 컨트롤러
│       └── resources/
│           └── application.properties
└── README.md
```

## 주요 개념

### 1. @Hint 커스텀 어노테이션
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hint {
    String value() default "no-hint";
    int level() default 1;
}
```
- **@Target(ElementType.METHOD)**: 메서드에만 적용
- **@Retention(RetentionPolicy.RUNTIME)**: RUNTIME에 유지되어 AOP에서 사용 가능

### 2. HintAspect - AOP Proxy 구현
```java
@Aspect
@Component
public class HintAspect {
    @Around("@annotation(hint)")
    public Object processHint(ProceedingJoinPoint joinPoint, Hint hint) throws Throwable {
        // Before: 메서드 실행 전
        // Proceed: 실제 메서드 실행
        // After: 메서드 실행 후
    }
}
```
- **@Around**: Before와 After를 모두 처리
- **@annotation(hint)**: @Hint 어노테이션이 붙은 메서드만 적용
- **ProceedingJoinPoint.proceed()**: 실제 메서드 실행

### 3. HintService - @Hint 적용
```java
@Service
public class HintService {
    @Hint(value = "Simple hint", level = 1)
    public String simpleProcess(String input) { ... }
    
    @Hint(value = "Complex hint", level = 3)
    public String complexProcess(String input) { ... }
    
    // @Hint 없음 - AOP 미적용
    public String normalProcess(String input) { ... }
}
```

## 실행 흐름

1. **요청 발생**
   ```
   GET /api/hint/simple?input=hello
   ```

2. **AOP Proxy가 요청을 가로챔** (HintAspect)
   - Before: "Hint detected! Value: Simple hint, Level: 1"
   - 실행 시간 측정 시작

3. **실제 메서드 실행** (HintService.simpleProcess)
   - "SimpleProcess 실행 중... Input: hello"
   - 반환값: "Processed: hello"

4. **Aspect 후처리** (After)
   - 실행 시간 출력
   - 반환값 출력

5. **클라이언트에게 응답**
   - "Processed: hello"

## API 엔드포인트

### 1. Level 1: 단순 처리 (@Hint 적용)
```bash
GET http://localhost:8080/api/hint/simple?input=hello
```
**결과**: AOP Proxy가 적용되어 로그 출력됨

### 2. Level 2: 중복 처리 (@Hint 적용)
```bash
GET http://localhost:8080/api/hint/medium?input=word&count=3
```
**결과**: AOP Proxy가 적용되어 로그 출력됨

### 3. Level 3: 복잡한 처리 (@Hint 적용)
```bash
GET http://localhost:8080/api/hint/complex?input=spring
```
**결과**: AOP Proxy가 적용되어 로그 + 실행 시간 출력됨

### 4. 일반 메서드 (@Hint 미적용)
```bash
GET http://localhost:8080/api/hint/normal?input=test
```
**결과**: AOP Proxy가 미적용되어 HintAspect 로그 없음

## 실행 방법

### Maven으로 실행
```bash
cd 08.aop-annotation
mvn spring-boot:run
```

### 빌드 및 실행
```bash
mvn clean package
java -jar target/springbootaopannotationsample-0.0.1-SNAPSHOT.jar
```

## 콘솔 로그 예시

```
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - === @Hint Aspect 시작 ===
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - 메서드명: simpleProcess
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - Hint value: Simple hint
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - Level: 1
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.service.HintService - SimpleProcess 실행 중... Input: hello
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - 메서드 실행 성공
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - 실행 시간: 5ms
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - 반환값: Processed: hello
2025-12-08 10:30:45 [http-nio-8080-exec-1] INFO  com.skala.aopanotation.aspect.HintAspect - === @Hint Aspect 종료 ===
```

## 학습 포인트

### 1. AOP Proxy 동작 원리
- Spring은 @Aspect가 붙은 클래스를 감지하여 자동으로 프록시 생성
- 메서드 호출 시 프록시를 거쳐 실제 메서드 호출
- @annotation 포인트컷으로 특정 어노테이션이 붙은 메서드만 가로챔

### 2. @Around의 동작
```
클라이언트 요청
    ↓
프록시 (Before 처리)
    ↓
@Around에서 처리
    ↓
joinPoint.proceed() - 실제 메서드 실행
    ↓
프록시 (After 처리)
    ↓
클라이언트에게 응답
```

### 3. @Hint vs 일반 메서드
- **@Hint 적용**: HintAspect의 @Around가 실행됨
- **일반 메서드**: AOP가 미적용되어 직접 호출됨

### 4. 어노테이션 속성 활용
- HintAspect에서 Hint 객체로 value와 level에 접근 가능
- 어노테이션 값에 따라 다른 처리 가능

## 주의사항

1. **public 메서드만 가능**: private, protected 메서드는 프록시 적용 불가
2. **같은 클래스 내부 호출**: 프록시를 거치지 않아 AOP 미적용
3. **Spring Bean이어야 함**: @Service, @Component 등이 필요
4. **@EnableAspectJAutoProxy**: AspectJ 자동 프록시 생성 활성화 필요

## 확장 가능성

- Level에 따라 다른 처리 구현
- 예외 발생 시 특별한 처리
- 실행 시간이 일정 이상일 때 알림
- 권한 검사 AOP 구현
