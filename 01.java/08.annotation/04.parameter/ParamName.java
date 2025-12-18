import java.lang.annotation.*;

/**
 * Parameter Annotation 정의
 * 메서드의 매개변수에만 적용 가능
 * 
 * 사용 예: @ParamName("userName") String name
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamName {
    String value();  // 매개변수의 의미있는 이름
}
