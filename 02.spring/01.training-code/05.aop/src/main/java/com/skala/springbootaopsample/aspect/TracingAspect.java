package com.skala.springbootaopsample.aspect;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class TracingAspect {
    
    // 서비스 계층 메소드를 대상으로 하는 포인트컷
    //@Pointcut("execution(* com.skala.springbootaopsample.service.*.*(..))")
    //public void serviceLayer() {}
    
    // 컨트롤러 계층 메소드를 대상으로 하는 포인트컷
    @Pointcut("execution(* com.skala.springbootaopsample.controller.*.*(..))")
    public void controllerLayer() {}
    
    // 트레이싱을 적용할 모든 메소드 (서비스 + 컨트롤러)
    //@Pointcut("serviceLayer() || controllerLayer()")
    @Pointcut("controllerLayer()")
    public void tracingTargets() {}
    
    @Around("tracingTargets()")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 시그니처 정보 가져오기
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
    
        
        // 시작 시간 기록
        long startTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTime = dateFormat.format(new Date(startTime));

        //log.info("→ [ENTER] {}.{}() with arguments: {}", className, methodName, argString);
        String message = String.format("→ [ENT] %s.%s start-time=%s", className, methodName, formattedTime);
        System.out.println("[Tracing Aspect: start]" + message );

        //sleep 1 second
        Thread.sleep(1000);
        
        try {
            // 실제 메소드 실행
            Object result = joinPoint.proceed();

            //sleep 1 second
            Thread.sleep(1000);
            
            // 종료 시간 기록 및 실행 시간 계산
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            // 결과 로깅 (결과가 너무 크면 처리)
            String resultString = (result != null) ? result.toString() : "null";
            if (resultString.length() > 100) {
                resultString = resultString.substring(0, 100) + "... (truncated)";
            }
            
            message = String.format("← [EXIT] %s.%s() returned: %s (executed in %dms)", className, methodName, resultString, executionTime);
            System.out.println("[Tracing Aspect: end]" + message);
            
            return result;
        } catch (Throwable t) {
            // 예외 발생 시 로깅
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            log.error("[Tracing Aspect: exception]! [ERROR] {}.{}() threw exception: {} (after {}ms)", 
                    className, methodName, t.getMessage(), executionTime);
            
            throw t;
        }
    }
}
