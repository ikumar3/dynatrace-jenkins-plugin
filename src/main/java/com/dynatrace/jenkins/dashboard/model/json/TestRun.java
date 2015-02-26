package com.dynatrace.jenkins.dashboard.model.json;

import java.util.List;

/**
 * Created by cwat-wgottesh on 23.02.2015.
 */
public class TestRun {
  private String category;
  private String id;
  private int numDegraded;
  private int numFailed;
  private int numImproved;
  private int numInvalidated;
  private int numPassed;
  private int numVolatile;
  private long startTime;
  private String systemProfile;
  private List<DtTestResult> testResults;
  private String versionMajor;
  private String versionMinor;
  private String versionMilestone;
  private String versionRevision;
  private String versionBuild;

  public String getCategory() {
    return category;
  }

  public String getId() {
    return id;
  }

  public int getNumDegraded() {
    return numDegraded;
  }

  public int getNumFailed() {
    return numFailed;
  }

  public int getNumImproved() {
    return numImproved;
  }

  public int getNumInvalidated() {
    return numInvalidated;
  }

  public int getNumPassed() {
    return numPassed;
  }

  public int getNumVolatile() {
    return numVolatile;
  }

  public long getStartTime() {
    return startTime;
  }

  public String getSystemProfile() {
    return systemProfile;
  }

  public List<DtTestResult> getTestResults() {
    return testResults;
  }

  public String getVersionMajor() {
    return versionMajor;
  }

  public String getVersionMinor() {
    return versionMinor;
  }

  public String getVersionMilestone() {
    return versionMilestone;
  }

  public String getVersionRevision() {
    return versionRevision;
  }

  public String getVersionBuild() {
    return versionBuild;
  }

  @Override
  public String toString() {
    return "TestRun{" +
            "category='" + category + '\'' +
            ", id='" + id + '\'' +
            ", numDegraded=" + numDegraded +
            ", numFailed=" + numFailed +
            ", numImproved=" + numImproved +
            ", numInvalidated=" + numInvalidated +
            ", numPassed=" + numPassed +
            ", numVolatile=" + numVolatile +
            ", startTime=" + startTime +
            ", systemProfile='" + systemProfile + '\'' +
            "[" + testResults + "]" +
            '}';
  }
}
