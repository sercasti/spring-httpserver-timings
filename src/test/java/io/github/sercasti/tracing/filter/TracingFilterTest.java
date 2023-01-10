package io.github.sercasti.tracing.filter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import io.github.sercasti.tracing.filter.TracingFilter;

/**
 * Unit tests for {@link TracingFilter}.
 */
public class TracingFilterTest {

    @Test
    public void testDoFilter() throws IOException, ServletException {
        final TracingFilter filter = new TracingFilter();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final FilterChain filterChain = mock(FilterChain.class);
        filter.doFilterInternal(request, response, filterChain);
        assertNull(TracingFilter.tracingLocal.get());
        assertNotNull(response.getHeader("Server-Timing"));
    }

}
