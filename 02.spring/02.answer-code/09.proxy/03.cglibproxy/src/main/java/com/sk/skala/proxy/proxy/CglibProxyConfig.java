package com.sk.skala.proxy.proxy;

import com.sk.skala.proxy.service.RealServiceImpl;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.Method;

@Configuration
public class CglibProxyConfig {

    @Bean
    @Primary
    public RealServiceImpl proxyService() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealServiceImpl.class); 
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                System.out.println("### CGLIB Proxy - Before: " + method.getName());
                Object result = proxy.invokeSuper(obj, args); // 실제 메서드 호출
                System.out.println("### CGLIB Proxy - After: " + method.getName());
                // 리턴값을 조작해보자
                if (result instanceof String) {
                    result = result + " [from proxy]";
                }
                
                return result;
            }
        });

        return (RealServiceImpl) enhancer.create(); // 프록시 인스턴스 생성
    }
}
