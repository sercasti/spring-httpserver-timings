package io.github.sercasti.tracing.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.sercasti.tracing.core.Metric;
import io.github.sercasti.tracing.core.Tracing;

@Aspect
@Component
public class TracingInterceptor {

    private final Tracing tracing;

    @Autowired
    public TracingInterceptor(final Tracing tracing) {
        this.tracing = tracing;
    }

    @Around("@annotation(com.urbit.tracing.Traceable)")
    public Object trace(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Metric metric = tracing.start(joinPoint.getSignature().getName());
        try {
            return joinPoint.proceed();
        } finally {
            metric.stop();
        }
    }

}