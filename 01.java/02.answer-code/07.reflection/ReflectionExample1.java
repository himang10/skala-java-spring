import java.lang.reflect.Method;

public class ReflectionExample1 {
    public static void main(String[] args) throws Exception {
        MyController controller = new MyController();

        // "hello" 메서드를 Reflection으로 실행
        Method method = MyController.class.getMethod("hello");
        String result = (String) method.invoke(controller);
        System.out.println(result); // 출력: Hello, Spring!
    }
}

class MyController {
    public String hello() {
        return "Hello, Spring!";
    }
}

