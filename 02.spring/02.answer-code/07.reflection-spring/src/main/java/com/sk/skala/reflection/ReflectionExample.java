package com.sk.skala.reflection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

@Component
public class ReflectionExample implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // 1. 클래스 정보 가져오기
        Class<?> personClass = Class.forName("com.sk.skala.reflection.Person");

        // 2. 기본 생성자로 인스턴스 생성
        Person person1 = (Person) personClass.getDeclaredConstructor().newInstance();

        // 3. private 생성자 접근
        Constructor<?> privateConstructor = personClass.getDeclaredConstructor(String.class, int.class);
        privateConstructor.setAccessible(true);
        Person person2 = (Person) privateConstructor.newInstance("John", 30);

        // 4. private 필드 접근 및 수정
        Field nameField = personClass.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(person1, "Alice");

        // 5. private 메소드 호출
        Method sayHelloMethod = personClass.getDeclaredMethod("sayHello");
        sayHelloMethod.setAccessible(true);
        sayHelloMethod.invoke(person1);

        // 6. 모든 필드 정보 출력
        System.out.println("\n모든 필드 정보:");
        Field[] fields = personClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("필드명: " + field.getName());
            System.out.println("타입: " + field.getType());
            System.out.println("접근제어자: " + Modifier.toString(field.getModifiers()));
        }

        // 7. 모든 메소드 정보 출력
        System.out.println("\n모든 메소드 정보:");
        Method[] methods = personClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("메소드명: " + method.getName());
            System.out.println("반환타입: " + method.getReturnType());
            System.out.println("파라미터 타입들: " + Arrays.toString(method.getParameterTypes()));
        }
    }
}
