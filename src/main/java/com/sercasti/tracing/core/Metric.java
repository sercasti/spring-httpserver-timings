package com.sercasti.tracing.core;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Metric {

    private final String name;
    private final String description;
    private final ZonedDateTime startTime;
    private Duration duration;

    public Metric(final String name, final String description) {
        this.name = name;
        this.description = description;
        this.startTime = ZonedDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void stop() {
        if (duration != null) {
            throw new IllegalStateException("Metric '" + name + "' was already stopped!");
        }
        duration = Duration.between(startTime, ZonedDateTime.now());
    }
}
