package skalajava;
import java.util.Arrays;
public class skalatest14 {
    public static void main(String[] args) {
        withStream();
    }
    public static void withStream(){
        String[] arrayOfStrings = {"A","B","C"};
        Arrays.stream(arrayOfStrings).forEach(System.out::println);
    }
}
