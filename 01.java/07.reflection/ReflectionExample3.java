import java.lang.reflect.Method;

class MyService {
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    public static Integer addNumbers(Integer a, Integer b) {
        return a + b;
    }
}

public class ReflectionExample3 {

    // 1. 인스턴스 메서드 호출
    public static Object invokeInstanceMethod(String className, String methodName, Object... args) throws Exception {
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // for-each 문으로 파라미터 타입 추출
        Class<?>[] paramTypes = new Class<?>[args.length];
        int i = 0;
        for (Object arg : args) {
            paramTypes[i++] = arg.getClass();
        }

        Method method = clazz.getMethod(methodName, paramTypes);
        return method.invoke(instance, args);
    }

    // 2. static 메서드 호출
    public static Object invokeStaticMethod(String className, String methodName, Object... args) throws Exception {
        Class<?> clazz = Class.forName(className);

        Class<?>[] paramTypes = new Class<?>[args.length];
        int i = 0;
        for (Object arg : args) {
            System.out.println("parameter type: " + arg.getClass());
            paramTypes[i++] = arg.getClass();
        }

        Method method = clazz.getMethod(methodName, paramTypes);
        return method.invoke(null, args);
    }

    public static void main(String[] args) throws Exception {
        // 인스턴스 메서드 호출
        Object result1 = invokeInstanceMethod("MyService", "sayHello", "Alice");
        System.out.println("sayHello result = " + result1);

        // static 메서드 호출
        Object result2 = invokeStaticMethod("MyService", "addNumbers", 5, 7);
        System.out.println("addNumbers result = " + result2);
    }
}
