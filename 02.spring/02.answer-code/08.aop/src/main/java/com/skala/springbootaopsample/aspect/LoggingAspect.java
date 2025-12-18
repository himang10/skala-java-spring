package com.skala.springbootaopsample.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;

@Aspect
@Component
public class LoggingAspect {

    // 서비스 계층 메소드를 대상으로 하는 포인트컷
    @Pointcut("execution(* com.skala.springbootaopsample.service.*.*(..))")
    public void serviceMethods() {}

    // 서비스 계층 메소드 호출 전에 로깅
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[Logging Aspect: logBefore] " + joinPoint.getSignature().getName());
    }

    // 서비스 계층 메소드 호출 후에 정상 처리 완료 시 로깅
    @AfterReturning("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("[Logging Aspect: logAfter]" + joinPoint.getSignature().getName());
    }

    // 서비스 계층 메소드 호출에 예외 발생 시 로깅
    @AfterThrowing("serviceMethods()")
    public void logAfterThrowing(JoinPoint joinPoint) {
        System.out.println("[Logging Aspect: logAfterThrowing] " + joinPoint.getSignature().getName());
    }

}