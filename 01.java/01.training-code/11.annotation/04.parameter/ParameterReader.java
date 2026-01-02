import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Field;

/**
 * Parameter Annotation을 이용한 자동 주입 유틸리티
 * 
 * 동작:
 * 1. JSON 객체(POJO)의 필드 조회
 * 2. @ParamName과 매칭되는 필드 찾기
 * 3. 메서드에 자동 주입
 */
public class ParameterReader {
    
    /**
     * JSON 객체에서 ParamName에 맞는 필드값을 추출하여 메서드 호출
     */
    public static void invokeWithParams(Object controller, String methodName, Object jsonData) 
            throws Exception {
        
        // 1. 메서드와 매개변수 조회
        Method method = controller.getClass().getDeclaredMethod(methodName, String.class, int.class);
        Parameter[] parameters = method.getParameters();
        
        // 2. 메서드의 매개변수에 맞는 값 준비
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            
            // @ParamName 확인
            if (param.isAnnotationPresent(ParamName.class)) {
                ParamName annotation = param.getAnnotation(ParamName.class);
                String fieldName = annotation.value();
                
                // JSON 객체의 필드에서 값 추출
                Field field = jsonData.getClass().getField(fieldName);
                args[i] = field.get(jsonData);
                
                System.out.println("주입: " + fieldName + " = " + args[i]);
            }
        }
        
        // 3. 메서드 호출
        System.out.println("메서드 호출: " + methodName);
        method.invoke(controller, args);
    }
}

