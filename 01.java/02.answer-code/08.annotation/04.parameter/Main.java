/**
 * Parameter Annotation 예제
 * 
 * 1. UserController의 createUser 메서드 정의
 * 2. 매개변수에 @ParamName 적용
 * 3. Reflection으로 매개변수 정보 읽기
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("\n=== Parameter Annotation 예제 ===\n");
        
        // 1. JSON 데이터 생성 (User 객체)
        System.out.println("1. JSON 데이터 생성\n");
        User user = new User("홍길동", 30);
        System.out.println("데이터: " + user + "\n");
        
        // 2. @ParamName과 매칭되는 필드에서 값 추출하여 메서드 호출
        System.out.println("2. ParamName 기반 자동 주입\n");
        UserController controller = new UserController();
        ParameterReader.invokeWithParams(controller, "createUser", user);
        
        System.out.println("\n완료!\n");
    }
}
