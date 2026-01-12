package com.skala.aopannotation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Hint 커스텀 어노테이션
 * 메서드에 적용하여 AOP Aspect에서 처리하도록 함
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hint {

    /**
     * 힌트 메시지
     */
    String value() default "no-hint";

    /**
     * 처리 레벨
     */
    int level() default 1;
}
