import java.lang.annotation.*;

/**
 * Method Annotation 정의
 * 메서드에만 적용 가능하며, 실행 순서를 지정할 수 있음
 * 
 * 사용 예: @RunMe(order = 1)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunMe {
    int order() default 0;  // 실행 순서 (숫자가 작을수록 먼저 실행)
}
