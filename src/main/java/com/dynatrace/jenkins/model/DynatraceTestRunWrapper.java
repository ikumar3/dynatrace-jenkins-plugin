package com.dynatrace.jenkins.model;

import com.dynatrace.jenkins.model.DynatraceTestRunInfo;

/**
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
