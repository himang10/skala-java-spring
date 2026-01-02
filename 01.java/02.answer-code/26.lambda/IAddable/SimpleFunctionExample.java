import java.util.function.Function;

public class SimpleFunctionExample {

    // Function<T, R>를 입력으로 받아 처리하는 메서드
    public static <T, R> R myfunc(T input, Function<T, R> fn) {
        return fn.apply(input);
    }

    public static void main(String[] args) {
        // 문자열 길이 구하기
        int length = myfunc("Hello", s -> s.length());
        System.out.println("문자열 길이: " + length);

        // 숫자 제곱 구하기
        int squared = myfunc(5, n -> n * n);
        System.out.println("제곱: " + squared);

        // 문자열을 대문자로 변환
        String upper = myfunc("lambda", s -> s.toUpperCase());
        System.out.println("대문자: " + upper);
    }
}
