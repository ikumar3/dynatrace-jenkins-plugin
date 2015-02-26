package com.dynatrace.jenkins.dashboard.model.json;

import java.util.List;

/**
 * Created by cwat-wgottesh on 23.02.2015.
 */
public class DtTestResult {
  private long execTime;
  private String name;
  private String pkg;
  private String status;
  private List<DtTestMeasure> measures;

  @Override
  public String toString() {
    return "DtTestResult{" +
            "execTime=" + execTime +
            ", measures=" + measures +
            '}';
  }
}
