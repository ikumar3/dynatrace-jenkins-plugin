package com.dynatrace.jenkins.builders;

import com.dynatrace.jenkins.model.DynatraceConnectionInfo;
import com.dynatrace.jenkins.model.DynatraceVersion;
import com.dynatrace.jenkins.rest.DynatraceServerEndpoint;
import com.dynatrace.jenkins.rest.DynatraceServerEndpointImpl;
import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.EnvironmentContributingAction;

/**
 * Created by cwat-wgottesh on 24.02.2015.
 */
public class TestrunIDContributorAction implements EnvironmentContributingAction {

  private String systemProfile;
  private DynatraceConnectionInfo dynatraceConnectionInfo;
  private DynatraceVersion dynatraceVersion;

  public TestrunIDContributorAction() {}

  public TestrunIDContributorAction(DynatraceConnectionInfo dynatraceConnectionInfo, String systemProfile, DynatraceVersion dynatraceVersion) {
    this.dynatraceConnectionInfo = dynatraceConnectionInfo;
    this.dynatraceVersion = dynatraceVersion;
    this.systemProfile = systemProfile;
  }

  @Override
  public void buildEnvVars(AbstractBuild<?, ?> abstractBuild, EnvVars envVars) {
    System.out.println("buildEnvVars!");
    DynatraceServerEndpoint dt = new DynatraceServerEndpointImpl(dynatraceConnectionInfo);
    dt.registerTestRun(systemProfile, dynatraceVersion);
  }

  @Override
  public String getIconFileName() {
    return null;
  }

  @Override
  public String getDisplayName() {
    return "BLABLA";
  }

  @Override
  public String getUrlName() {
    return null;
  }
}
