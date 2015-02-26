package com.dynatrace.jenkins.dashboard.model.json;

/**
 * Created by cwat-wgottesh on 23.02.2015.
 */
public class DtTestMeasure {
  private double expectedMax;
  private double expectedMin;
  private String metricGroup;
  private String name;
  private int numDegradedRuns;
  private int numFailingOrInvalidatedRuns;
  private int numImprovedRuns;
  private int numValidRuns;
  private String unit;
  private double value;
  private double violationPercentage;

  @Override
  public String toString() {
    return "DtTestMeasure{" +
            "expectedMax=" + expectedMax +
            ", expectedMin=" + expectedMin +
            ", metricGroup='" + metricGroup + '\'' +
            ", name='" + name + '\'' +
            ", numDegradedRuns=" + numDegradedRuns +
            ", numFailingOrInvalidatedRuns=" + numFailingOrInvalidatedRuns +
            ", numImprovedRuns=" + numImprovedRuns +
            ", numValidRuns=" + numValidRuns +
            ", unit='" + unit + '\'' +
            ", value=" + value +
            ", violationPercentage=" + violationPercentage +
            '}';
  }
}
