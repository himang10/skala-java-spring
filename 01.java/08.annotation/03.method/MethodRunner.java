import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Method Annotation을 인식하고 실행하는 실행기
 * 
 * 동작:
 * 1. @RunMe가 붙은 메서드 찾기
 * 2. order 값으로 정렬
 * 3. 순서대로 실행
 */
public class MethodRunner {
    
    public static void run(Object target) throws Exception {
        Class<?> clazz = target.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        
        // @RunMe가 붙은 메서드만 필터링
        Method[] runMethods = Arrays.stream(methods)
            .filter(m -> m.isAnnotationPresent(RunMe.class))
            .toArray(Method[]::new);
        
        // order 값으로 정렬 (작은 순서부터)
        Arrays.sort(runMethods, new Comparator<Method>() {
            public int compare(Method m1, Method m2) {
                int order1 = m1.getAnnotation(RunMe.class).order();
                int order2 = m2.getAnnotation(RunMe.class).order();
                return Integer.compare(order1, order2);
            }
        });
        
        // 정렬된 순서대로 메서드 실행
        for (Method method : runMethods) {
            RunMe annotation = method.getAnnotation(RunMe.class);
            System.out.println("[Order " + annotation.order() + "] " + method.getName() + " 실행");
            method.invoke(target);
        }
    }
}
