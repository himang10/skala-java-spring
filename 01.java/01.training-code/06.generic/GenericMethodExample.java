public class GenericMethodExample {
    // 제네릭 메서드 정의
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }

    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4, 5};
        String[] stringArray = {"Hello", "World"};

        System.out.println("Integer Array:");
        printArray(intArray); // Integer 배열 출력

        System.out.println("\nString Array:");
        printArray(stringArray); // String 배열 출력
    }
}