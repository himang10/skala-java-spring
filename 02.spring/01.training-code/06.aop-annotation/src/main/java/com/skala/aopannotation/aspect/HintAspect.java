package com.skala.aopannotation.aspect;

import com.skala.aopannotation.annotation.Hint;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Hint 어노테이션을 처리하는 Aspect
 * RUNTIME 시점에 프록시를 통해 메서드를 가로챔
 */
@Slf4j
@Aspect
@Component
public class HintAspect {

    /**
     * @Hint 어노테이션이 붙은 메서드가 실행될 때 동작
     * @Around는 Before와 After를 모두 처리할 수 있음
     */
    @Around("@annotation(hint)")
    public Object processHint(ProceedingJoinPoint joinPoint, Hint hint) throws Throwable {
        
        // 1. 메서드 실행 전 처리 (Before)
        String methodName = joinPoint.getSignature().getName();
        String hintValue = hint.value();
        int level = hint.level();
        
        log.info("=== @Hint Aspect 시작 ===");
        log.info("메서드명: {}", methodName);
        log.info("Hint value: {}", hintValue);
        log.info("Level: {}", level);
        
        long startTime = System.currentTimeMillis();

        // 2. 실제 타겟 메서드 실행
        Object result = null;
        try {
            result = joinPoint.proceed();
            log.info("메서드 실행 성공");
        } catch (Exception e) {
            log.error("메서드 실행 중 예외 발생: {}", e.getMessage());
            throw e;
        }

        // 3. 메서드 실행 후 처리 (After)
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        log.info("실행 시간: {}ms", executionTime);
        log.info("반환값: {}", result);
        log.info("=== @Hint Aspect 종료 ===");
        
        return result;
    }
}
