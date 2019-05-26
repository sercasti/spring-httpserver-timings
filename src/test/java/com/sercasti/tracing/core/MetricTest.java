package com.sercasti.tracing.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.sercasti.tracing.core.Metric;

/**
 * Unit tests for {@link Metric}.
 */
public class MetricTest {
    private static final String NAME = "John";
    private static final String DESC = "Lennon";
    
    @Test
    public void testShouldEqualParams()  {
        final Metric metric = new Metric(NAME, DESC);
        assertEquals(metric.getName(), NAME);
        assertEquals(metric.getDescription(), DESC);
        assertNotNull(metric.getStartTime());
        assertNull(metric.getDuration());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testShouldNotStop() {
        final Metric metric = new Metric(NAME, DESC);
        metric.stop();
        assertNotNull(metric.getDuration());
        metric.stop();
    }

}
