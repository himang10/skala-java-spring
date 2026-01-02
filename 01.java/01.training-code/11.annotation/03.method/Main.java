/**
 * Method Annotation 예제
 * 
 * 1. TaskRunner에 @RunMe가 붙은 메서드들 정의
 * 2. MethodRunner.run()으로 실행
 * 3. order 값 순서대로 실행됨
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("\n=== Method Annotation 예제 ===\n");
        
        // 1. TaskRunner 객체 생성
        TaskRunner runner = new TaskRunner();
        System.out.println("1. TaskRunner 객체 생성\n");
        
        // 2. @RunMe 메서드 자동 찾기 및 순서대로 실행
        System.out.println("2. @RunMe 메서드 실행 (order 순서로)\n");
        MethodRunner.run(runner);
        
        System.out.println("\n(noAnnotation은 @RunMe가 없으므로 실행되지 않음)\n");
        System.out.println("완료!\n");
    }
}
