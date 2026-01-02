import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Person {
    private String name;
    private List<String> hobbies;

    public Person(String name, List<String> hobbies) {
        this.name = name;
        this.hobbies = hobbies;
    }

    public List<String> getHobbies() {
        return hobbies;
    }
}

public class FlatMapExample {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person("Alice", Arrays.asList("Reading", "Swimming")),
                new Person("Bob", Arrays.asList("Cycling", "Hiking")),
                new Person("Charlie", Arrays.asList("Gaming", "Cooking"))
        );

        // List<String> mapHobbies = people.stream()
        //                              .map(person -> person.getHobbies())
        //                              .collect(Collectors.toList());
        // System.out.println("mapHobbies:");
        // System.out.println(mapHobbies);

        List<String> flatmapHobbies = people.stream()
                                        .flatMap(person -> person.getHobbies().stream()) // 각 사람의 취미 리스트를 펼침
                                        .collect(Collectors.toList());
        System.out.println("flatmapHobbies:");
        System.out.println(flatmapHobbies);
        // 출력: [Reading, Swimming, Cycling, Hiking, Gaming, Cooking]
    }
}
