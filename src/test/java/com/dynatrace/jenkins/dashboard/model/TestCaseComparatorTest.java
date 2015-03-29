package com.dynatrace.jenkins.dashboard.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
/**
 * Created by wolfgang on 23.03.2015.
 */
public class TestCaseComparatorTest {

    /**
     * Test that TestCase instances are sorted using the TestCaseComparator
     */
    @Test
    public void testStatusSorting() {
        Set<TestCase> unsortedTestCases = new HashSet<TestCase>();
        Set<TestCase> sortedTestCases = new TreeSet<TestCase>(new TestCaseComparator());


        TestCase passedTest = new TestCase("I am a passed test");
        passedTest.setStatus(TestCaseStatus.PASSED);
        unsortedTestCases.add(passedTest);

        TestCase volatileTest = new TestCase("I am a volatile test");
        volatileTest.setStatus(TestCaseStatus.VOLATILE);
        unsortedTestCases.add(volatileTest);

        TestCase failedTest = new TestCase("I am a failure");
        failedTest.setStatus(TestCaseStatus.FAILED);
        unsortedTestCases.add(failedTest);

        TestCase invalidatedTest = new TestCase("I am an invalidated test");
        invalidatedTest.setStatus(TestCaseStatus.INVALIDATED);
        unsortedTestCases.add(invalidatedTest);

        TestCase improvedTest = new TestCase("I am an improved test");
        improvedTest.setStatus(TestCaseStatus.IMPROVED);
        unsortedTestCases.add(improvedTest);

        for (TestCase tc : unsortedTestCases) {
            sortedTestCases.add(tc);
        }

        Iterator<TestCase> iterator = sortedTestCases.iterator();
        assertThat("failed test must be first in sorted set", iterator.next(), sameInstance(failedTest));
        assertThat("volatile test must be next in sorted set", iterator.next(), sameInstance(volatileTest));
        assertThat("improved test must be next in sorted set", iterator.next(), sameInstance(improvedTest));
        assertThat("passed test must be next in sorted set", iterator.next(), sameInstance(passedTest));
        assertThat("invalidated test must be next in sorted set", iterator.next(), sameInstance(invalidatedTest));
        assertThat("No more items left to iterate", iterator.hasNext(), equalTo(false));
    }

    /**
     * Test that the TestCaseComparator works
     */
    @Test
    public void testComparingTwoTestCases() {
        TestCase failedTest = new TestCase("I am a failure");
        failedTest.setStatus(TestCaseStatus.FAILED);

        TestCase passedTest = new TestCase("I am a passed test");
        passedTest.setStatus(TestCaseStatus.PASSED);

        TestCaseComparator cmp = new TestCaseComparator();
        int cmpResult = cmp.compare(failedTest, passedTest);
        assertThat("comparison result must be -4", cmpResult, equalTo(-4));
    }
}
