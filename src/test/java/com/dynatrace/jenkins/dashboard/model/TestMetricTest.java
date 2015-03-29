package com.dynatrace.jenkins.dashboard.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.print.attribute.standard.DateTimeAtCompleted;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by wolfgang on 22.03.2015.
 */
public class TestMetricTest {
    private static final String RED_ICON = "red.gif";
    private static final String GREEN_ICON = "green.gif";
    private TestMetric responseTimeMetric;
    private TestMetric noExceptionMetric;

    @Before
    public void createTestMetrics() {
        responseTimeMetric = TestMetricHelper.createResponseTimeMetric();
        noExceptionMetric = TestMetricHelper.createNoExceptionsMetric();
    }

    /**
     * Test that a test metric contains expected values
     */
    @Test
    public void testTestMetricGetters() {
        assertThat("metric must be called \"Response Time\"", responseTimeMetric.getMeasure(), equalTo("Response Time"));
        assertThat("metric must in metric group called \"Timings\"", responseTimeMetric.getMetricgroup(), equalTo("Timings"));
        assertThat("metric must have unit ms ", responseTimeMetric.getUnit(), equalTo("ms"));
        assertThat("metric must have value 900", responseTimeMetric.getValue(), equalTo("900"));
    }

    /**
     * Test that compareTo works as expected, looking at the measure and metricgroup properties
     */
    @Test
    public void testCompareToNotEqualMetrics() {
        int cmp = responseTimeMetric.compareTo(noExceptionMetric);
        assertThat("metrics must not be equal", cmp, not(equalTo(0)));
        assertThat("compareTo must return 15", cmp, equalTo(15));
    }


    /**
     * Test that compareTo works as expected, looking at the measure and metricgroup properties
     */
    @Test
    public void testCompareToEqualMetrics() {
        TestMetric anotherResponseTimeMetric = TestMetricHelper.createResponseTimeMetric();
        assertThat("measures must be equal", anotherResponseTimeMetric.getMeasure(), equalTo(responseTimeMetric.getMeasure()));
        assertThat("metric groups must be equal", anotherResponseTimeMetric.getMetricgroup(), equalTo(responseTimeMetric.getMetricgroup()));
        assertThat("objects must not be identical", anotherResponseTimeMetric, not(sameInstance(responseTimeMetric)));
        int cmp = responseTimeMetric.compareTo(anotherResponseTimeMetric);
        assertThat("objects are equals, compareTo must return 0", cmp, equalTo(0));
    }

    /**
     * Test that a failed test gets a red icon
     */
    @Test
    public void testGetIconForFailedTestMetric() {
        responseTimeMetric.setFailed(true);
        assertThat("failed test must have a red icon", responseTimeMetric.getIcon(), equalTo(RED_ICON));
    }

    /**
     * Test a passing test gets a green icon
     */
    @Test
    public void testGetIconForPassedTestMetric() {
        responseTimeMetric.setFailed(false);
        assertThat("passed test metric must have a green icon", responseTimeMetric.getIcon(), equalTo(GREEN_ICON));
    }

    /**
     * Test that a non-numeric string with a period (.) throws an expection
     */
    @Rule public ExpectedException thrown = ExpectedException.none();
    @Test(expected = NumberFormatException.class)
    public void testSetValueFailsIfParameterNotDouble() {
        TestMetric tm = new TestMetric();
        tm.setValue("test.test test");
        thrown.expectMessage("For input string: \"test.test test\"");
    }


    /**
     * Test that values without "." are just stored as is
     */
    @Test
    public void testSetValueWithString() {
        TestMetric tm = new TestMetric();
        tm.setValue("untouched");
        assertThat("value must not be parsed", tm.getValue(), equalTo("untouched"));
        tm.setValue("5");
        assertThat("value must not be parsed", tm.getValue(), equalTo("5"));
    }

    /**
     * Test that values with "." somewhere always get two digits after the comma
     */
    @Test
    public void testSetValueWithDouble() {
        TestMetric tm = new TestMetric();
        tm.setValue("5.0");
        assertThat("numeric value must have two digits after the comma", tm.getValue(), equalTo("5.00"));
        tm.setValue("1.00");
        assertThat("numeric value must have two digits after the comma", tm.getValue(), equalTo("1.00"));
        tm.setValue("99.123456789");
        assertThat("numeric value must have two digits after the comma", tm.getValue(), equalTo("99.12"));
    }

    /**
     * Test that setTestrun only accepts valid timestamps
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetTestrunWithInvalidTimestamp() {
        noExceptionMetric.setTestRun("abcdef", "100", "false");
    }


    /**
     * Test that setTestrun accepts valid timestamps and sets the expected properties
     */
    @Test
    public void testSetTestrun() {
        noExceptionMetric.setTestRun("2015-03-23T20:51:24.755+01:00", "100.12", "false");
        assertThat("metric must have timestamp 23.03.2015 20:51:24", noExceptionMetric.getFormattedTimestamp(), equalTo("23.03.2015 20:51:24"));
        assertThat("metric must not be marked as failed", noExceptionMetric.isFailed(), equalTo(false));
        assertThat("metric must have value 100.12", noExceptionMetric.getValue(), equalTo("100.12"));
    }
}
