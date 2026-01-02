public class AnonymousExample {
    public static void main(String[] args) {
        // Runnable 인터페이스를 익명 클래스로 구현
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from anonymous class");
            }
        };
        runTask(task);
    }

    // 실제 동작하는 메서드
    public static void runTask(Runnable task) {
        task.run();
    }
}
