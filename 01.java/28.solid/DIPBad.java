// 저수준 모듈 (세부 구현)
class Keyboard {
    public void type() {
        System.out.println("키보드를 사용하여 입력합니다.");
    }
}

class Monitor {
    public void display() {
        System.out.println("모니터에 화면을 출력합니다.");
    }
}

// 고수준 모듈 (비즈니스 로직) ─ DIP 위반: 구현체에 직접 의존 + 직접 생성
class Computer {
    private Keyboard keyboard;
    private Monitor monitor;

    public Computer() {
        this.keyboard = new Keyboard(); // 직접적인 의존성
        this.monitor = new Monitor();   // 직접적인 의존성
    }

    public void operate() {
        keyboard.type();
        monitor.display();
    }
}

public class DIPBad {
    public static void main(String[] args) {
        Computer computer = new Computer();
        computer.operate();
    }
}

