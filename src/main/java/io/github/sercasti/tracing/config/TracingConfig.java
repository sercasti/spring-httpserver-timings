package io.github.sercasti.tracing.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import io.github.sercasti.tracing.core.Tracing;
import io.github.sercasti.tracing.filter.TracingFilter;

@Configuration
public class TracingConfig {

    @Bean(name = "tracing")
    @RequestScope
    public static Tracing createTracing() {
        return (Tracing) Proxy.newProxyInstance(TracingConfig.class.getClassLoader(), new Class[]{Tracing.class}, new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                return method.invoke(TracingFilter.getCurrentTiming(), args);
            }
        });
    }
}
