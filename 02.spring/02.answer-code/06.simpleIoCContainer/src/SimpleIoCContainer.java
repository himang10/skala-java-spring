import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class SimpleIoCContainer {
    private final Map<Class<?>, Object> beanRegistry = new HashMap<>();

    /** 특정 Bean 가져오기 */
    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beanRegistry.get(clazz));
    }

    /**  Bean을 가져오는 메서드 추가 */
    public <T> T registerBean(Class<T> clazz) throws Exception {

        //1. 이미 등록된 경우 반환
        if (beanRegistry.containsKey(clazz)) {
            return clazz.cast(beanRegistry.get(clazz));
        }

        //2. 클래스의 public 생성자 가져오기
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalStateException("No public constructor found for class: " + clazz.getName());
        }

        Constructor<?> constructor = constructors[0]; // 첫 번째 public 생성자 선택
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (Class<?> paramType : parameterTypes) {
            System.out.println("parameter type: " + paramType);
        }

        //3. 생성자 파라미터 타입을 기반으로 의존성 주입할 객체 찾기
        Object[] dependencies = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            dependencies[i] = registerBean(parameterTypes[i]); // 🔥 의존성을 먼저 등록하고 가져오기
        }

        //4.  객체 생성 및 등록 (생성자 기반 의존성 주입)
        T instance = clazz.cast(constructor.newInstance(dependencies));
        beanRegistry.put(clazz, instance);

        return instance;

    }

    /** 전체 Bean 목록 출력 */
    public void printAllBeans() {
        System.out.println("Registered Beans:");
        beanRegistry.forEach((key, value) -> System.out.println(" - " + key.getName() + " -> " + value));
    }

    /** IoC Container 와 유사하게 동작하는 코드 부분 */
    public static void main(String[] args) throws Exception {
        SimpleIoCContainer container = new SimpleIoCContainer();

        // 1. Dependency, MyService, MyController 자동 등록
        container.registerBean(Dependency.class);
        container.registerBean(MyService.class);
        container.registerBean(MyController.class);

        // 2. 전체 등록된 Bean 목록 출력
        container.printAllBeans();

        System.out.println("\n\ncall registerd Bean.handleRequest:");

        // 3. MyController 실행
        MyController controller = container.getBean(MyController.class);
        controller.handleRequest();

        // 4. MyService 실행
        MyService service = container.getBean(MyService.class);
        service.handleRequest();

    }
}

