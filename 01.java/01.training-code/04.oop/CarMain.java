
class Car {
    String brand;
    String color;
    int speed;

    // 생성자(Constructor)로 초기화
    public Car(String brand, String color, int speed) {
        // this를 사용해서 속성 설정
        this.brand = brand;
        this.color = color;
        this.speed = speed;
    }

    // 동작(메서드) 추가
    public void drive() {
        System.out.print("Driving at " + speed);
        System.out.print(" Brand: " + brand);
        System.out.print(" Color: " + color);
        System.out.println();
    }
}

public class CarMain {
    public static void main(String[] args) {
        Car carH = new Car("Hyundai", "Black", 100);
        Car carK = new Car("Kia", "White", 120);
        carH.drive();
        carK.drive();
    }
}

