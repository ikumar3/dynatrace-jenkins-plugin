package com.dynatrace.jenkins.model;

/**
 * Wrapper that points to a testRun. Seems to be the easiest way to support the JSON format the Dynatrace
 * Server returns...
 *
 * Created by cwat-wgottesh on 26.02.2015.
 */
public class DynatraceTestRunWrapper {
  private DynatraceTestRunInfo testRun;

  public DynatraceTestRunInfo getTestRun() {
    return testRun;
  }

  public void setTestRun(DynatraceTestRunInfo testRun) {
    this.testRun = testRun;
  }
}
