// 추상 클래스 정의
abstract class Animal {
    String name;

    // 생성자
    public Animal(String name) {
        this.name = name;
    }

    // 추상 메서드 (하위 클래스에서 반드시 구현해야 함)
    abstract void makeSound();

    // 일반 메서드 (공통된 기능 제공 가능)
    public void sleep() {
        System.out.println(name + " is sleeping...");
    }
}