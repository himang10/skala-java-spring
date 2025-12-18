package skalajava;
public class skalatest13 {
    public static void main(String[] args) {
        withoutStream();
    }
    public static void withoutStream() {
        String[] arrayOfStrings = {"A", "B", "C"};
        for(String name: arrayOfStrings) {
            System.out.println(name);
        }
    }
}
