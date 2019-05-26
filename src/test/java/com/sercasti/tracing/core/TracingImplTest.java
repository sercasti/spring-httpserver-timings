package com.sercasti.tracing.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.sercasti.tracing.core.Metric;
import com.sercasti.tracing.core.TracingImpl;

/**
 * Unit tests for {@link TracingImpl}.
 */
public class TracingImplTest {

    private static final String NAME = "cache";
    private static final String DESC = "Cache Read";

    @Test
    public void testStart() {
        final TracingImpl tracing = new TracingImpl();
        final Metric metric = tracing.start(NAME, DESC);
        assertEquals(metric.getName(), NAME);
        assertEquals(metric.getDescription(), DESC);
        assertNotNull(metric.getStartTime());
        assertNull(metric.getDuration());
        assertFalse(tracing.metrics.isEmpty());
    }

    @Test
    public void testDump() throws InterruptedException {
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final TracingImpl tracing = new TracingImpl();
        final Metric metric = tracing.start(NAME, DESC);
        Thread.sleep(100);
        metric.stop();
        final Metric metric2 = tracing.start("db", "db query");
        metric2.stop();
        tracing.dump(response);
        verify(response, atLeastOnce()).addHeader(anyString(), anyString());
        assertTrue(tracing.metrics.isEmpty());
    }

    @Test
    public void testConvert() throws InterruptedException {
        final TracingImpl tracing = new TracingImpl();
        final Metric metric = tracing.start(NAME, DESC);
        Thread.sleep(100);
        metric.stop();
        assertTrue(tracing.convert(metric).startsWith("cache;desc=\"Cache Read\";dur="));
    }

}
