/** 
 * DI (Dependency Injection) 예제 코드 
 * 
 * - Coffee 클래스는 Ame 인터페이스를 통해 의존성을 주입받음
 * - Hot과 Ice는 Ame 인터페이스를 구현하여 구체적인 객체 역할을 수행
 * - 생성자 주입(Constructor Injection) 방식 적용
 */

public class DI1{      
    public static void main(String[] args) {     
        // Hot과 Ice 객체 생성 (의존성 인스턴스화)
        Hot hot = new Hot();
        Ice ice = new Ice();

        // Coffee 객체에 외부에서 의존성을 주입 (DI 적용)
        Coffee iceCoffee = new Coffee(ice);
        Coffee hotCoffee = new Coffee(hot);

        System.out.println("[1] ICE Coffee :");
        iceCoffee.getType(); // Ice 객체가 주입된 Coffee 객체 호출

        System.out.println("[2] HOT Coffee :");
        hotCoffee.getType(); // Hot 객체가 주입된 Coffee 객체 호출
    }  
}

/**
 * Coffee 클래스: Ame 인터페이스를 통해 의존성을 주입받음 (DI 적용)
 */
class Coffee {
    private Ame ame; // 인터페이스 타입으로 참조 (구체적인 구현체와 분리)

    // 생성자를 통한 의존성 주입 (Constructor Injection)
    public Coffee(Ame ame) {
        this.ame = ame;
    }

    // 주입된 객체의 메서드를 호출
    public void getType() {
        ame.getInfo();
    }
}

/**
 * Americano 인터페이스: 다양한 커피 타입을 위한 공통 인터페이스
 */
interface Ame {
    void getInfo(); // 구현체에서 각 커피의 정보를 출력하도록 정의
}

/**
 * Hot 클래스: Ame 인터페이스 구현 (구체적인 커피 타입 1)
 */
class Hot implements Ame {
    public void getInfo() {
        System.out.println("Hot Americano");
    }
}

/**
 * Ice 클래스: Ame 인터페이스 구현 (구체적인 커피 타입 2)
 */
class Ice implements Ame {
    public void getInfo() {
        System.out.println("Ice Americano");
    }
}

