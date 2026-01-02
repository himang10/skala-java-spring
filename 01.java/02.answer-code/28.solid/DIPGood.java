// 추상화(인터페이스) — 고수준/저수준 모두가 이 추상화에 의존
interface InputDevice {
    void type();
}
interface Display {
    void display();
}

// 저수준 모듈(구현체)
class Keyboard implements InputDevice {
    @Override
    public void type() {
        System.out.println("키보드를 사용하여 입력합니다.");
    }
}
class Monitor implements Display {
    @Override
    public void display() {
        System.out.println("모니터에 화면을 출력합니다.");
    }
}

// 고수준 모듈(비즈니스 로직) — 구현이 아닌 '추상화'에 의존
class Computer {
    private final InputDevice inputDevice;
    private final Display display;

    // 의존성 주입(생성자 주입)
    public Computer(InputDevice inputDevice, Display display) {
        this.inputDevice = inputDevice;
        this.display = display;
    }

    public void operate() {
        inputDevice.type();
        display.display();
    }
}

public class DIPGood {
    public static void main(String[] args) {
        // 조립(Composition Root)에서만 구현체를 선택
        InputDevice keyboard = new Keyboard();
        Display monitor = new Monitor();

        Computer computer = new Computer(keyboard, monitor);
        computer.operate();
    }
}

