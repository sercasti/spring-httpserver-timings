package io.github.sercasti.tracing.core;

/**
 * Service to add metrics of the server to a http response.
 */
public interface Tracing {

    static final String SERVER_TIMING_HEADER = "Server-Timing";

    /**
     * Create a new metric with the current time as start time and the given name.
     * 
     * @param name the name of the metric
     * @return the metric
     */
    default Metric start(String name) {
        return start(name, null);
    }

    /**
     * Create a new metric with the current time as start time, the given name and
     * description.
     * 
     * @param name        the name of the metric
     * @param description the description of the metric
     * @return the metric
     */
    Metric start(String name, String description);

}
