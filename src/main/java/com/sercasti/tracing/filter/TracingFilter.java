package com.sercasti.tracing.filter;

import java.io.IOException;
import java.time.Duration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sercasti.tracing.core.Metric;
import com.sercasti.tracing.core.Tracing;
import com.sercasti.tracing.core.TracingImpl;
import com.sercasti.tracing.domain.HttpServletResponseCopier;

@Component
@Order(1)
public class TracingFilter extends OncePerRequestFilter {

    static final ThreadLocal<TracingImpl> tracingLocal = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        final HttpServletResponseCopier responseWrapper = new HttpServletResponseCopier(response);
        final TracingImpl tracing = new TracingImpl();
        tracingLocal.set(tracing);
        try {
            final Metric totalMetric = tracing.start("total", "total duration of the request");
            filterChain.doFilter(request, responseWrapper);
            totalMetric.stop();
        } finally {
            responseWrapper.flushBuffer();
            tracing.dump(response);
            tracingLocal.set(null);
            responseWrapper.reallyFlush();
        }
    }

    public static Tracing getCurrentTiming() {
        final TracingImpl timing = tracingLocal.get();
        if (timing == null) {
            return new Tracing() {
                @Override
                public Metric start(final String name, final String description) {
                    return new Metric(name, description) {
                        @Override
                        public String getName() {
                            return name;
                        }

                        @Override
                        public String getDescription() {
                            return description;
                        }

                        @Override
                        public Duration getDuration() {
                            return null;
                        }

                        @Override
                        public void stop() throws IllegalStateException {

                        }
                    };
                }
            };
        }
        return timing;
    }

}
