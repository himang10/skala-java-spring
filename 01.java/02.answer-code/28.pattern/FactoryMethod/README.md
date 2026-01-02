# Factory Method Pattern (팩토리 메서드 패턴)

## 개요
Factory Method 패턴은 객체 생성을 처리하는 생성 패턴입니다. 객체 생성 로직을 서브클래스로 위임하여, 클라이언트 코드가 구체적인 클래스에 의존하지 않고 인터페이스에 의존하도록 합니다.

## 구조

### 1. Product (Pizza)
- 팩토리 메서드로 생성될 객체의 공통 인터페이스
- 모든 구체적인 제품이 구현해야 하는 메서드 정의

### 2. ConcreteProduct (CheesePizza, PepperoniPizza, VeggiePizza)
- Product 인터페이스를 구현하는 구체적인 제품 클래스
- 실제로 생성되는 객체들

### 3. Creator (PizzaStore)
- Factory Method를 선언하는 추상 클래스
- `createPizza()` - 서브클래스가 구현해야 하는 팩토리 메서드
- `orderPizza()` - 팩토리 메서드를 사용하는 템플릿 메서드

### 4. ConcreteCreator (NYPizzaStore, ChicagoPizzaStore)
- Creator를 상속받아 팩토리 메서드를 구현
- 어떤 ConcreteProduct를 생성할지 결정

## 장점

1. **느슨한 결합 (Loose Coupling)**
   - 클라이언트 코드가 구체적인 클래스가 아닌 인터페이스에 의존
   - 새로운 제품 추가 시 기존 코드 수정 최소화

2. **단일 책임 원칙 (SRP)**
   - 객체 생성 코드를 한 곳에 집중
   - 제품 생성 로직을 별도로 관리

3. **개방-폐쇄 원칙 (OCP)**
   - 새로운 제품 타입 추가 시 기존 코드 수정 없이 확장 가능
   - 새로운 ConcreteCreator와 ConcreteProduct만 추가

4. **유연성**
   - 런타임에 생성할 객체 타입 결정 가능
   - 서브클래스에서 객체 생성 방식 커스터마이징

## 사용 시나리오

- 어떤 클래스가 자신이 생성해야 하는 객체의 클래스를 미리 알 수 없을 때
- 클래스가 생성할 객체의 사양을 서브클래스가 지정하기를 원할 때
- 생성 로직이 복잡하거나 변경 가능성이 있을 때
- 객체 생성과 사용을 분리하고 싶을 때

## 실행 방법

```bash
# 컴파일
javac *.java

# 실행
java PizzaTestDrive
```

## 예상 출력

```
===== Order 1: NY Cheese Pizza =====
Creating NY Style cheese Pizza
Preparing Cheese Pizza
Adding cheese topping...
Baking Cheese Pizza at 350 degrees for 25 minutes
Cutting Cheese Pizza into diagonal slices
Placing Cheese Pizza in official PizzaStore box
Ordered a Cheese Pizza

===== Order 2: Chicago Pepperoni Pizza =====
Creating Chicago Style pepperoni Pizza
Preparing Pepperoni Pizza
Adding pepperoni topping...
Baking Pepperoni Pizza at 350 degrees for 25 minutes
Cutting Pepperoni Pizza into diagonal slices
Placing Pepperoni Pizza in official PizzaStore box
Ordered a Pepperoni Pizza
...
```

## 주요 개념

### Factory Method vs Simple Factory
- **Simple Factory**: 단순히 팩토리 클래스를 사용 (패턴이 아님)
- **Factory Method**: 상속을 통해 객체 생성을 서브클래스에 위임 (디자인 패턴)

### Template Method와의 관계
- `orderPizza()` 메서드는 Template Method 패턴을 사용
- 알고리즘의 골격은 정의하되, 일부 단계는 서브클래스에 위임

## 확장 가능성

새로운 피자 스타일 추가:
```java
public class CaliforniaPizzaStore extends PizzaStore {
    @Override
    protected Pizza createPizza(String type) {
        // California 스타일 피자 생성 로직
    }
}
```

새로운 피자 타입 추가:
```java
public class SupremePizza implements Pizza {
    // Supreme 피자 구현
}
```
