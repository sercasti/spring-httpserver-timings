package io.github.sercasti.tracing.filter;

import java.io.IOException;
import java.time.Duration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.sercasti.tracing.core.Metric;
import io.github.sercasti.tracing.core.Tracing;
import io.github.sercasti.tracing.core.TracingImpl;
import io.github.sercasti.tracing.domain.HttpServletResponseCopier;

@Component
@Order(1)
public class TracingFilter extends OncePerRequestFilter {

    static final ThreadLocal<TracingImpl> tracingLocal = new ThreadLocal<>();

    @Value("${tracing.disabled}")
    private final boolean disabled = false;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        if(disabled) {
            filterChain.doFilter(request, response);
        }

        final String chainingHeaders = request.getHeader(Tracing.SERVER_TIMING_HEADER);
        final HttpServletResponseCopier responseWrapper = new HttpServletResponseCopier(response);
        final TracingImpl tracing = new TracingImpl();
        tracingLocal.set(tracing);
        try {
            final Metric totalMetric = tracing.start("total", "total duration of the request");
            filterChain.doFilter(request, responseWrapper);
            totalMetric.stop();
        } finally {
            responseWrapper.flushBuffer();
            tracing.dump(response, chainingHeaders);
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
