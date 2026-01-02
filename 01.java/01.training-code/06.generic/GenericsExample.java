public class GenericsExample {
    public static void main(String[] args) {
        // Integer 타입의 Box
        Box<Integer> integerBox = new Box<>();
        integerBox.setItem(10);
        System.out.println("Integer Value: " + integerBox.getItem());

        // String 타입의 Box
        Box<String> stringBox = new Box<>("Hello, Generics!");
        System.out.println("String Value: " + stringBox.getItem());
    }
}