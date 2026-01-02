public class AbstractMain {
    public static void main(String[] args) {
        // 추상 클래스는 직접 인스턴스화 불가능
        // Animal animal = new Animal("Some Animal"); // 오류 발생

        Animal dog = new Dog("Buddy");
        Animal cat = new Cat("Kitty");

        dog.makeSound(); // Buddy says: Woof! Woof!
        cat.makeSound(); // Kitty says: Meow~

        dog.sleep(); // Buddy is sleeping...
        cat.sleep(); // Kitty is sleeping...
    }
}
