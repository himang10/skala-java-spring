# Builder Pattern (빌더 패턴)

## 개요
Builder 패턴은 복잡한 객체의 생성 과정을 단계별로 분리하여, 동일한 생성 절차에서 서로 다른 표현 결과를 만들 수 있도록 하는 생성 패턴입니다.

**Effective Java**에서 권장하는 Fluent Builder 방식을 사용합니다.

## 구조

1. **Product (Computer)**
   - 최종적으로 생성될 복잡한 객체
   - Private constructor로 직접 생성 방지
   - 모든 필드를 `final`로 선언하여 불변성 보장

2. **Builder (Computer.Builder)**
   - Static nested class로 구현
   - Fluent interface 제공 (method chaining)
   - 필수 파라미터는 생성자로 받음
   - 선택적 파라미터는 메서드로 설정
   - `build()` 메서드로 최종 객체 생성

## 사용 예제

```java
// 모든 옵션 지정
Computer gamingComputer = new Computer.Builder("Intel i9", "64GB")
    .storage("2TB SSD")
    .graphicsCard("RTX 4090")
    .powerSupply("1000W")
    .hasWifi(true)
    .build();

// 필수 옵션만 지정 (나머지는 기본값)
Computer simpleComputer = new Computer.Builder("Intel i5", "16GB")
    .build();
```

## 장점

1. **복잡한 객체 생성 과정 단순화**
   - 생성자 파라미터가 많을 때 가독성 향상
   - 텔레스코핑 생성자 패턴(Telescoping Constructor) 문제 해결

2. **불변 객체 생성**
   - Setter 없이 객체 생성 가능
   - Thread-safe한 불변 객체 생성에 이상적

3. **유연성**
   - 동일한 생성 과정으로 다른 표현의 객체 생성
   - 필수/선택적 파라미터 명확히 구분

4. **가독성**
   - 메서드 체이닝으로 코드 가독성 향상
   - 각 속성의 의미가 명확함

5. **단계별 생성**
   - 객체 생성을 여러 단계로 나누어 실행 가능
   - 중간 상태 제어 가능

## 단점

1. **코드 복잡도 증가**
   - Builder 클래스 추가로 코드량 증가
   - 간단한 객체에는 오버엔지니어링

2. **메모리 오버헤드**
   - Builder 객체 생성으로 추가 메모리 사용

## 사용 시나리오

Builder 패턴을 사용해야 할 때:
- 생성자 파라미터가 4개 이상일 때
- 많은 선택적 파라미터가 있을 때
- 불변 객체를 생성해야 할 때
- 객체 생성 과정이 복잡할 때
- 같은 생성 과정으로 다른 표현의 객체를 만들어야 할 때

## Telescoping Constructor Pattern과의 비교

**Telescoping Constructor (안티패턴):**
```java
public Computer(String cpu, String ram) { ... }
public Computer(String cpu, String ram, String storage) { ... }
public Computer(String cpu, String ram, String storage, String gpu) { ... }
// 계속 늘어남...
```

**Builder Pattern:**
```java
new Computer.Builder(cpu, ram)
    .storage(storage)
    .graphicsCard(gpu)
    .build();
```

## JavaBeans Pattern과의 비교

**JavaBeans Pattern (문제점):**
```java
Computer computer = new Computer();
computer.setCpu("Intel i9");  // 객체가 일관성 없는 상태가 될 수 있음
computer.setRam("64GB");
// Mutable, Thread-unsafe
```

**Builder Pattern:**
```java
Computer computer = new Computer.Builder("Intel i9", "64GB")
    .build();  // Immutable, Thread-safe
```

## 실행 방법

```bash
# 컴파일
javac *.java

# 실행
java BuilderTestDrive
```

## 예상 출력

```
========================================
Builder Pattern Demo
========================================

===== 1. Fluent Builder Pattern =====
Building a Gaming Computer...

Computer Specifications:
  CPU: Intel Core i9-13900K
  RAM: 64GB DDR5
  Storage: 2TB NVMe SSD
  Graphics Card: NVIDIA RTX 4090
  Power Supply: 1000W 80+ Gold
  Motherboard: ASUS ROG Maximus Z790
  Cooling System: Liquid Cooling
  WiFi: Yes
  Bluetooth: Yes

Building an Office Computer...

Computer Specifications:
  CPU: Intel Core i5-13400
  RAM: 16GB DDR4
  Storage: 512GB SSD
  Graphics Card: Integrated
  Power Supply: Standard
  Motherboard: Standard
  Cooling System: Air Cooling
  WiFi게이밍 컴퓨터 =====

Computer Specifications:
  CPU: Intel Core i9-13900K
  RAM: 64GB DDR5
  Storage: 2TB NVMe SSD
  Graphics Card: NVIDIA RTX 4090
  Power Supply: 1000W 80+ Gold
  Motherboard: ASUS ROG Maximus Z790
  Cooling System: Liquid Cooling
  WiFi: Yes
  Bluetooth: Yes

===== 사무용 컴퓨터 =====
### Validation (유효성 검증)
- `build()` 메서드에서 최종 객체 생성 전 검증
- 잘못된 상태의 객체 생성 방지

## 실무 적용 예

- **Lombok `@Builder` 어노테이션**: 자동으로 Builder 패턴 생성
실제 프로젝트에서 Builder 패턴이 사용되는 예:

- **Lombok `@Builder` 어노테이션**: 자동으로 Builder 패턴 생성
- **StringBuilder/StringBuffer**: Java 표준 라이브러리
- **Stream API**: `Stream.builder()`
- **HttpClient**: Request 빌더
- **RestTemplate**: UriComponentsBuilder
- **JPA Criteria API**: Query 빌더

## 확장 예제

Builder에 새로운 옵션 추가:
```java
public Builder soundCard(String soundCard) {
    this.soundCard = soundCard;
    return this;
}

public Builder hasRgbLighting(boolean hasRgbLighting) {
    this.hasRgbLighting = hasRgbLighting;
    return this;
}
```