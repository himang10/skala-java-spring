public class NumberBox {
    public static void main(String[] args) {
        int a = 100;
        int b = 100;

        Integer A = 200;
        Integer B = 200;

        // 기본형 비교 (값 비교)
        System.out.println("a == b: " + (a == b));

        // 래퍼 클래스 비교 (주소 비교)
        System.out.println("A == B: " + (A == B));

        // 래퍼 클래스 equals()로 비교 (값 비교)
        System.out.println("A.equals(B): " + A.equals(B));
    }
}