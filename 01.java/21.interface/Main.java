package skalajava;

public class Main {
    public static void main(String[] args) {
        Animal dog = new Dog();
        Animal cat = new Cat();
        
        dog.makeSound(); // 출력: 멍멍!
        cat.makeSound(); // 출력: 야옹!

        handleAnimalSound(cat);
    }

    static void handleAnimalSound(Animal animal){
        animal.makeSound();
    }
}
