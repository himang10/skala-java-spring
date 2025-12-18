package com.skala.basic.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class ApiLoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(ApiLoggingAspect.class);

  // 모든 RestController의 public 메서드에 적용
  @Around("execution(public * com.skala.basic.controller..*Controller.*(..))")
  public Object logApiRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    // 1. 요청 파라미터 로깅
    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();
    log.info("[API REQUEST] {} | Params: {}", methodName, args);

    // 2. 실제 메서드 실행
    Object result = joinPoint.proceed();

    long end = System.currentTimeMillis();
    long duration = end - start;

    // 3. 응답, 처리시간 로깅
    log.info("[API RESPONSE] {} | Result: {} | Duration: {}ms", methodName, result, duration);

    return result;
  }
}
