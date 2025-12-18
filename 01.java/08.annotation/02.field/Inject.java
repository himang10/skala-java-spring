import java.lang.annotation.*;

/**
 * Field Injection Annotation 정의
 * - @Target: 필드에만 적용
 * - @Retention: 런타임까지 유지
 * 
 * 사용 예: @Inject private OrderService orderService;
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
