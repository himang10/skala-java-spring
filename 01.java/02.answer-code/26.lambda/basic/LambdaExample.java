public class LambdaExample {
    public static void main(String[] args) {
        // Runnable 인터페이스를 익명 클래스로 구현
        Runnable task = () -> System.out.println("Hello from lambda");
        runTask(task);
    }

    // 실제 동작하는 메서드
    public static void runTask(Runnable task) {
        task.run();
    }
}
