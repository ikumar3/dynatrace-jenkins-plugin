package com.dynatrace.jenkins.dashboard.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wolfgang on 23.03.2015.
 */
public class TestMetricHelper {
    public static TestMetric createResponseTimeMetric() {
        TestMetric tm1 = new TestMetric();
        tm1.setMeasure("Response Time");
        tm1.setMetricgroup("Timings");
        tm1.setUnit("ms");
        tm1.setValue("900");
        return tm1;
    }

    public static TestMetric createNoExceptionsMetric() {
        TestMetric tm2 = new TestMetric();
        tm2.setMeasure("Number of Exceptions");
        tm2.setMetricgroup("Exceptions");
        tm2.setUnit("count");
        tm2.setValue("3");
        return tm2;
    }

    public static Set<TestMetric> createTestMetrics() {
        Set<TestMetric> metrics = new HashSet<TestMetric>();
        metrics.add(createResponseTimeMetric());
        metrics.add(createNoExceptionsMetric());

        return metrics;
    }
}
