/**
 * UserController 클래스
 * 메서드 매개변수에 @ParamName 어노테이션 적용
 */
public class UserController {
    
    public void createUser(
            @ParamName("userName") String name,
            @ParamName("userAge") int age) {
        System.out.println("사용자 생성: " + name + ", 나이: " + age);
    }
}
