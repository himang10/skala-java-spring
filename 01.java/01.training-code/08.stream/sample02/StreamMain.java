import java.util.List;

public class StreamMain {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        numbers.stream()
                .map(n -> {
                    System.out.println("Mapping: " + n);
                    return n * 2;
                })
                .filter(n -> {
                    System.out.println("Filtering: " + n);
                    return n > 5;
                })
                .forEach(n -> System.out.printf("## forEach: %d %n%n", n));
    }
    
}
