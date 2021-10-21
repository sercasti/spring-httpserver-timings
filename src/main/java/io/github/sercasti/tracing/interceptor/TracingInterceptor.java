package io.github.sercasti.tracing.interceptor;

import io.github.sercasti.tracing.Traceable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.sercasti.tracing.core.Metric;
import io.github.sercasti.tracing.core.Tracing;

import java.lang.reflect.Method;

@Aspect
@Component
public class TracingInterceptor {

    private final Tracing tracing;

    @Autowired
    public TracingInterceptor(final Tracing tracing) {
        this.tracing = tracing;
    }

    @Around("@annotation(io.github.sercasti.tracing.Traceable)")
    public Object trace(final ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Traceable traceable = signature.getMethod().getAnnotation(Traceable.class);
        String traceName = traceable.name().isEmpty() ? signature.getName() : traceable.name();
        String traceDescription = traceable.description().isEmpty() ? null : traceable.description();

        final Metric metric = tracing.start(traceName, traceDescription);

        try {
            return joinPoint.proceed();
        } finally {
            metric.stop();
        }
    }

}