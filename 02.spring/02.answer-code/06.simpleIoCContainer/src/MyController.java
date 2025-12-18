public class MyController {
    private final Dependency dependency;

    // 생성자 주입
    public MyController(Dependency dependency) {
        this.dependency = dependency;
    }

    public void handleRequest() {
        dependency.doSomething(this.getClass().getSimpleName());
    }
}
