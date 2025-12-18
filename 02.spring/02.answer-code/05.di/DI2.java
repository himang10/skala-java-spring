/** DI (Dependency Injection) 예제 코드 **/

public class DI2 {      
    public static void main(String[] args) {     

        // Wheel 객체 생성 (의존성 생성)
        Wheel FrontRightWheel = new Wheel(100, 110);          
        Wheel FrontLeftWheel = new Wheel(200, 210);              

        // Car 객체 생성 시 Wheel 객체를 주입 (DI 적용)
        Car race_car  = new Car(FrontLeftWheel, FrontRightWheel);     

        // 주입된 Wheel 객체의 정보를 출력
        race_car.getInfo(); 
    }  
}

// Wheel 클래스: 자동차의 바퀴를 나타내는 클래스
class Wheel {     
    private double size, diameter;      

    // 생성자를 통해 Wheel의 크기와 지름을 설정
    Wheel(double size, double diameter) {         
        this.size = size;         
        this.diameter = diameter;     
    }     

    // 바퀴 정보 출력 메서드
    public void getInfo() {         
        System.out.print("Size = " + this.size + " ");         
        System.out.println("Diameter = " + this.diameter);     
    } 
}

// Car 클래스: 자동차를 나타내며, Wheel 객체를 의존성으로 가짐
class Car {     
    // 자동차의 앞바퀴 (왼쪽, 오른쪽) 의존성
    private Wheel FrontLeftWheel, FrontRightWheel;     

    // 생성자를 통해 Wheel 객체를 주입받음 (의존성 주입)
    public Car(Wheel FLW, Wheel FRW) { 
        System.out.println("Car Object Created");     

        this.FrontLeftWheel = FLW;     
        System.out.println("Front Left Wheel Injected");     
                                                 
        this.FrontRightWheel = FRW;     
        System.out.println("Front Right Wheel Injected");       
    }     

    // 주입된 Wheel 객체의 정보를 출력하는 메서드
    public void getInfo() {         
        this.FrontLeftWheel.getInfo();         
        this.FrontRightWheel.getInfo();

    }
}

