import java.util.ArrayList;
import java.util.List;

interface Animal {
    void makeSound();
}

class Dog implements Animal {
    public void makeSound() { System.out.println("멍멍"); }
}

class Tiger implements Animal {
    public void makeSound() { System.out.println("어흥"); }
}

class Cat implements Animal {
    public void makeSound() { System.out.println("야옹"); }
}

class Pig implements Animal {
    public void makeSound() { System.out.println("꿀꿀"); }
}

public class Ocp {
    public static void main(String[] args) {
        // List에 동물 등록
        List<Animal> animals = new ArrayList<>();
        animals.add(new Dog());
        animals.add(new Tiger());
        animals.add(new Cat());
        animals.add(new Pig());

        // Stream + forEach 로 울음소리 출력
        animals.stream()
               .forEach(Animal::makeSound);
    }
}