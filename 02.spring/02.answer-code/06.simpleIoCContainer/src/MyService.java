public class MyService {
    private final Dependency dependency;

    // 생성자 주입
    public MyService(Dependency dependency) {
        this.dependency = dependency;
    }

    public void handleRequest() {
        dependency.doSomething(this.getClass().getSimpleName());
    }
}
