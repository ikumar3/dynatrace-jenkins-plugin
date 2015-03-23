package com.dynatrace.jenkins.rest;

import com.dynatrace.jenkins.model.DynatraceTestRunInfo;
import com.dynatrace.jenkins.model.DynatraceVersion;

/**
 * Created by cwat-wgottesh on 24.02.2015.
 */
public interface DynatraceServerEndpoint {
  public void connect();
  public DynatraceTestRunInfo registerTestRun(String systemProfile, DynatraceVersion version);

  TestConnectionResult testConnection(String systemProfile);

  String getDashboardReport(String dashboardName);
}
