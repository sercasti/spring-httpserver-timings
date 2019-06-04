package io.github.sercasti.tracing.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

public class TracingImpl implements Tracing {

    private static final String SERVER_TIMING_HEADER_DUR = "dur=";
    private static final String SERVER_TIMING_HEADER_DESC = "desc=";
    private final List<Metric> metrics = new ArrayList<>();

    public Metric start(final String name, final String description) {
        final Metric metric = new Metric(name, description);
        metrics.add(metric);
        return metric;
    }

    public void dump(final HttpServletResponse response, final String chainingHeaders) {
        final String content = metrics.stream().map(m -> convert(m)).reduce("",(metrica, metricb) -> metrica + "," + metricb);
        final String union = Stream.of(chainingHeaders, content.substring(1)).filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(" "));
        if (union.length() > 0) {
            response.addHeader(SERVER_TIMING_HEADER, union);
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
