public class BaseCode {
    public static void main(String[] args) {
        // 그냥 메서드 호출 (람다나 익명 클래스 전혀 사용 안함)
        runTask();
    }

    // 실제 동작하는 메서드
    public static void runTask() {
        System.out.println("Hello from runTask");
    }
}
