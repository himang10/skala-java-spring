import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Person {
    public String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {
        this.name = "default";
        this.age = 0;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

public class ReflectionAPI {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Person.class;

        Constructor<?>[] constructors = clazz.getConstructors();
        Method[] methods = clazz.getDeclaredMethods();
        Field[] fields = clazz.getDeclaredFields();
        int modifiers = clazz.getModifiers();
        int fieldModifiers = clazz.getDeclaredField("name").getModifiers();


        for (Constructor<?> constructor : constructors) {
            System.out.println("생성자: " + constructor);
        }


        for (Method method : methods) {
            System.out.println("메서드: " + method.getName());
        }

        for (Field field : fields) {
            System.out.println("필드: " + field.getName() + " (타입: " + field.getType().getName() + ")");
        }

        System.out.println("public 클래스인가? " + Modifier.isPublic(modifiers));
        System.out.println("abstract 클래스인가? " + Modifier.isAbstract(modifiers));

        for (Field field : fields) {
            System.out.println("public 필드인가? " + Modifier.isPublic(fieldModifiers));
        }
    }
}
