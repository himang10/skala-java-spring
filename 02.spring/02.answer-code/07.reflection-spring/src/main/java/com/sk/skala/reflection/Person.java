package com.sk.skala.reflection;

// 예제용 Person 클래스
public class Person {
    private String name;
    private int age;

    public Person() {}

    private Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private void sayHello() {
        System.out.println("Hello, I'm " + name);
    }
}
