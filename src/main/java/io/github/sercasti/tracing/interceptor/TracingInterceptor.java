package io.github.sercasti.tracing.interceptor;

import io.github.sercasti.tracing.Traceable;
import io.github.sercasti.tracing.core.Metric;
import io.github.sercasti.tracing.core.Tracing;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        String traceName = StringUtils.isEmpty(traceable.name()) ? signature.getName() : traceable.name();
        String traceDescription = StringUtils.isEmpty(traceable.description()) ? null : traceable.description();

        final Metric metric = tracing.start(traceName, traceDescription);

        try {
            return joinPoint.proceed();
        } finally {
            metric.stop();
        }
    }

}