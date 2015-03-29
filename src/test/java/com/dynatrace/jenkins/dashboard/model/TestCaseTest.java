package com.dynatrace.jenkins.dashboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by wolfgang on 22.03.2015.
 */
public class TestCaseTest {
    private TestCase testCaseWithoutMetrics;
    private TestCase testCaseWithMetrics;



    /**
     * Create two dummy test cases, one with and one without metrics
     */
    @Before
    public void createTestCases() {
        testCaseWithoutMetrics = new TestCase("EmptyTest");
        testCaseWithMetrics = new TestCase("MyTest");
        Set<TestMetric> metrics = TestMetricHelper.createTestMetrics();
        for (TestMetric tm : metrics) {
            testCaseWithMetrics.addTestMetric(tm);
        }
    }

    /**
     * Test that the expected number of test metrics is present in the test case
     */
    @Test
    public void testGetTestCaseMetricsReturn2Items() {
        Set<TestMetric> metrics = testCaseWithMetrics.getTestMetrics();
        assertThat("test case must contain 2 metrics", metrics.size(), equalTo(2));
    }

    /**
     * Test that the list of test metrics is not modifiable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableGetTestCaseMetrics() {
        Set<TestMetric> metrics = testCaseWithMetrics.getTestMetrics();
        metrics.add(new TestMetric());
    }

    /**
     * Test that a test metric can be added to and retrieved from a test case,
     * and that a test metric is unique based on the metricgroup and measure fields
     * and can only be added once to a test case.
     */
    @Test
    public void testAddTestMetric(){
        TestMetric tm = new TestMetric();
        tm.setValue("1.00");
        tm.setUnit("count");
        tm.setMeasure("TestMeasure");
        tm.setMetricgroup("TestMetricgroup");
        testCaseWithoutMetrics.addTestMetric(tm);
        Set<TestMetric> metrics = testCaseWithoutMetrics.getTestMetrics();
        assertThat("test case must not be null", metrics, notNullValue());
        assertThat("test case must contain 1 metric", metrics.size(), equalTo(1));

        // check that the metric in the set is the one we've put in
        for (TestMetric metric : metrics) {
            assertThat("objects must be identical", metric, sameInstance(tm));
            assertThat("values must be equal", metric.getValue(), equalTo(tm.getValue()));
            assertThat("units must be equal", metric.getUnit(), equalTo(tm.getUnit()));
            assertThat("measures must be equal", metric.getMeasure(), equalTo(tm.getMeasure()));
            assertThat("metricgroups must be equal", metric.getMetricgroup(), equalTo(tm.getMetricgroup()));
        }

        // check that a metric is unique based on Measure and Metricgroup field -> won't be added a second time
        TestMetric tm2 = new TestMetric();
        tm2.setValue("2");
        tm2.setUnit("ms");
        tm2.setMeasure("TestMeasure");
        tm2.setMetricgroup("TestMetricgroup");
        testCaseWithoutMetrics.addTestMetric(tm2);
        metrics = testCaseWithoutMetrics.getTestMetrics();
        assertEquals(1, metrics.size());

        // check that the metric in the set is still the first one
        for (TestMetric metric : metrics) {
            assertThat("objects must be identical", metric, sameInstance(tm));
            assertThat("values must be equal", metric.getValue(), equalTo(tm.getValue()));
            assertThat("units must be equal", metric.getUnit(), equalTo(tm.getUnit()));
            assertThat("measures must be equal", metric.getMeasure(), equalTo(tm.getMeasure()));
            assertThat("metricgroups must be equal", metric.getMetricgroup(), equalTo(tm.getMetricgroup()));
        }
    }

    /**
     * Test that the test case gets a green icon when it's in status PASSED
     */
    @Test
    public void testGetIconForPassed() {
        testCaseWithMetrics.setStatus(TestCaseStatus.PASSED);
        assertEquals(testCaseWithMetrics.getIcon(), "green.gif");
    }

    /**
     * Test that the test case gets a yellow icon when it's in status VOLATILE
     */
    @Test
    public void testGetIconForVolatile() {
        testCaseWithMetrics.setStatus(TestCaseStatus.VOLATILE);
        assertEquals(testCaseWithMetrics.getIcon(), "yellow.gif");
    }

    /**
     * Test that the test case gets a red icon when it's in status DEGRADED
     */
    @Test
    public void testGetIconForDegraded() {
        testCaseWithMetrics.setStatus(TestCaseStatus.DEGRADED);
        assertEquals(testCaseWithMetrics.getIcon(), "red.gif");
    }

    /**
     * Test that the test case gets a red icon when it's in status FAILED
     */
    @Test
    public void testGetIconForFailed(){
        testCaseWithMetrics.setStatus(TestCaseStatus.FAILED);
        assertEquals(testCaseWithMetrics.getIcon(), "red.gif");
    }

    /**
     * Test that the test case gets a red icon when it's in status NONE
     */
    @Test
    public void testGetIconForNone(){
        testCaseWithMetrics.setStatus(TestCaseStatus.NONE);
        assertEquals(testCaseWithMetrics.getIcon(), "red.gif");
    }

    /**
     * Test that the test case gets a red icon when it's in status IMPROVED
     */
    @Test
    public void testGetIconForImproved(){
        testCaseWithMetrics.setStatus(TestCaseStatus.IMPROVED);
        assertEquals(testCaseWithMetrics.getIcon(), "red.gif");
    }

    /**
     * Test that the test case gets a red icon when it's in status INVALIDATED
     */
    @Test
    public void testGetIconForInvalidated(){
        testCaseWithMetrics.setStatus(TestCaseStatus.INVALIDATED);
        assertEquals(testCaseWithMetrics.getIcon(), "red.gif");
    }

    /**
     * Test that the test case gets a red icon when it does not have a status
     */
    @Test
    public void testGetIconForMissingStatus(){
        assertEquals(testCaseWithMetrics.getIcon(), "red.gif");
    }
}
