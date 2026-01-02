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

    // Producer → extends (읽기)
    static void readAnimals(List<? extends Animal> list) {
        Animal a = list.get(0);   // OK
        a.makeSound();

        // list.add(new Dog());   // 컴파일 에러 (의도적으로 주석)
    }

    // Consumer → super (쓰기)
    static void addDog(List<? super Dog> list) {
        list.add(new Dog());      // OK

        Object o = list.get(0);   // 읽기는 Object만 가능
        System.out.println("Added object type: " + o.getClass().getSimpleName());
    }
}

