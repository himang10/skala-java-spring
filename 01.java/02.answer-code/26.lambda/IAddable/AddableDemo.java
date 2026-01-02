@FunctionalInterface
interface IAddable<T> {
    T add(T a1, T a2);
}

public class AddableDemo {

    // 제네릭 메서드: 어떤 타입이든 처리 가능
    public static <T> void addGeneric(IAddable<T> adder, T a1, T a2) {
        System.out.println(adder.add(a1, a2));
    }

    public static void main(String[] args) {
        // 문자열 결합
        addGeneric((s1, s2) -> s1 + s2, "Hello, ", "World!");

        // 정수 덧셈
        addGeneric((i1, i2) -> i1 + i2, 10, 20);

        // Double 타입도 가능 (예시: 곱셈)
        addGeneric((d1, d2) -> d1 * d2, 3.5, 2.0);
    }
}