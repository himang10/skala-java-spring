package com.sk.skala.proxy.proxy;

import com.sk.skala.proxy.service.MyService;
import com.sk.skala.proxy.service.RealServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Configuration
public class JdkProxyConfig {

    @Bean
    @Primary
    public MyService proxyService() {
        RealServiceImpl target = new RealServiceImpl();

        return (MyService) Proxy.newProxyInstance(
                MyService.class.getClassLoader(),
                new Class[]{MyService.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("#### JDK Proxy - Before method: " + method.getName());
                        Object result = method.invoke(target, args);
                        System.out.println("#### JDK Proxy - After method: " + method.getName());

                        // return 값 조작
                        if (result instanceof String) {
                            result = ((String) result).toUpperCase() + "[from proxy]";
                        }
                        return result;
                    }
                }
        );
    }
}
