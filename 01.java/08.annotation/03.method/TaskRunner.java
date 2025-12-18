/**
 * TaskRunner 클래스
 * @RunMe 어노테이션이 붙은 메서드들을 정의
 */
public class TaskRunner {
    
    @RunMe(order = 2)
    public void taskB() {
        System.out.println("Task B 실행");
    }
    
    @RunMe(order = 1)
    public void taskA() {
        System.out.println("Task A 실행");
    }
    
    // 어노테이션 없음 - 실행되지 않음
    public void noAnnotation() {
        System.out.println("Ignore");
    }
}
