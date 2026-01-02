import java.util.ArrayList;
import java.util.List; 

public class WildCardDemo {

    public static void main(String[] args) {

        // =========================
        // <? extends T> : 읽기용
        // =========================
        List<Dog> dogList = List.of(new Dog());
        readAnimals(dogList);

        // =========================
        // <? super T> : 쓰기용
        // =========================
        List<Animal> animalList = new ArrayList<>();
        addDog(animalList);

        // 확인
        for (Animal a : animalList) {
            a.makeSound();
        }
    }
}

