import java.lang.reflect.Field;

/**
 * 의존성 주입 컨테이너
 * @Inject 필드를 찾아서 자동으로 객체를 생성하고 주입합니다.
 */
public class SimpleContainer {
    
    /**
     * 객체의 @Inject 필드에 자동으로 의존성을 주입
     */
    public static void inject(Object target) throws Exception {
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            // @Inject 어노테이션 확인
            if (field.isAnnotationPresent(Inject.class)) {
                // private 필드 접근 허용
                field.setAccessible(true);
                
                // 필드의 타입으로 객체 생성
                Class<?> fieldType = field.getType();
                Object instance = fieldType.getDeclaredConstructor().newInstance();
                
                // 필드에 객체 주입
                field.set(target, instance);
                
                System.out.println("   ✓ " + field.getName() + " 주입 완료");
            }
        }
    }
}
