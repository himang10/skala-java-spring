package com.skala.aopannotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring Boot AOP Annotation 샘플 애플리케이션
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class AopAnnotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AopAnnotationApplication.class, args);
	}

}
