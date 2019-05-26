package io.github.sercasti.tracing.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

public class TracingImpl implements Tracing {

    static final String SERVER_TIMING_HEADER = "Server-Timing";
    private static final String SERVER_TIMING_HEADER_DUR = "dur=";
    private static final String SERVER_TIMING_HEADER_DESC = "desc=";
    List<Metric> metrics = new ArrayList<>();

    public Metric start(final String name, final String description) {
        final Metric metric = new Metric(name, description);
        metrics.add(metric);
        return metric;
    }

    public void dump(final HttpServletResponse response) {
        final String headerName = SERVER_TIMING_HEADER;
        final String content = metrics.stream().map(m -> convert(m)).reduce("",(metrica, metricb) -> metrica + "," + metricb);
        if (content.length() > 0) {
            response.addHeader(headerName, content.substring(1));
        }
        metrics.clear();
    }

    String convert(final Metric metric) {
        final Duration duration = metric.getDuration();
        final String description = metric.getDescription();

        final String durPart = Optional.ofNullable(duration)
                .map(d -> ";" + SERVER_TIMING_HEADER_DUR + (Math.max(1.0, d.toNanos()) / 1000000.0)).orElse("");
        final String descPart = Optional.ofNullable(description)
                .map(d -> ";" + SERVER_TIMING_HEADER_DESC + "\"" + d + "\"").orElse("");

        return metric.getName() + descPart + durPart;
    }
}
