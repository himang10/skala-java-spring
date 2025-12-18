package com.skala.aop.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProbeAspect {

    @Pointcut("execution(* com.skala.aop.demo.service..*(..))")
    public void svc(){}

    @Before("svc()")
    public void probe(JoinPoint jp) {
        var sig = jp.getSignature();
        var ms  = (org.aspectj.lang.reflect.MethodSignature) sig;

        System.out.println("=== JoinPoint Probe ===");
        System.out.println("getSignature():          " + sig);                  // 기본 toString
        System.out.println(" - toShortString():      " + sig.toShortString());
        System.out.println(" - toLongString():       " + sig.toLongString());
        System.out.println(" - declaringTypeName:    " + sig.getDeclaringTypeName());
        System.out.println(" - methodName:           " + ms.getName());
        System.out.println(" - returnType:           " + ms.getReturnType().getName());
        System.out.println(" - parameterTypes:       " + java.util.Arrays.toString(ms.getParameterTypes()));
        System.out.println("getArgs():               " + java.util.Arrays.toString(jp.getArgs()));
        System.out.println("getTarget():             " + jp.getTarget().getClass().getName());
        System.out.println("getThis():               " + jp.getThis().getClass().getName());
        System.out.println("getKind():               " + jp.getKind());
        System.out.println("========================");
    }
}
