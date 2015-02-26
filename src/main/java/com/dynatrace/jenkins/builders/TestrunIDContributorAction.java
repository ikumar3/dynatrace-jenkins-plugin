package com.dynatrace.jenkins.builders;

import com.dynatrace.jenkins.model.DynatraceConnectionInfo;
import com.dynatrace.jenkins.model.DynatraceTestRunInfo;
import com.dynatrace.jenkins.model.DynatraceVersion;
import com.dynatrace.jenkins.rest.DynatraceServerEndpoint;
import com.dynatrace.jenkins.rest.DynatraceServerEndpointImpl;
import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.EnvironmentContributingAction;

import java.io.PrintStream;

/**
 * Created by cwat-wgottesh on 24.02.2015.
 */
public class TestrunIDContributorAction implements EnvironmentContributingAction {
  public static final String TESTRUN_ID_KEY = "DYNATRACE_TESTRUN_ID";
  public static final String TESTRUN_HREF_KEY = "DYNATRACE_TESTRUN_HREF";

  private PrintStream logger;
  private String systemProfile;
  private DynatraceConnectionInfo dynatraceConnectionInfo;
  private DynatraceVersion dynatraceVersion;

  public TestrunIDContributorAction(PrintStream logger, DynatraceConnectionInfo dynatraceConnectionInfo, String systemProfile, DynatraceVersion dynatraceVersion) {
    this.dynatraceConnectionInfo = dynatraceConnectionInfo;
    this.dynatraceVersion = dynatraceVersion;
    this.systemProfile = systemProfile;
    this.logger = logger;
  }

  @Override
  public void buildEnvVars(AbstractBuild<?, ?> abstractBuild, EnvVars envVars) {
    logger.println("Dynatrace: registering test run on " + dynatraceConnectionInfo.getHost() + ", system profile "
            + systemProfile + ", version " + dynatraceVersion.getVersionAsString());
    DynatraceServerEndpoint dt = new DynatraceServerEndpointImpl(dynatraceConnectionInfo);
    DynatraceTestRunInfo info = dt.registerTestRun(systemProfile, dynatraceVersion);
    if (info != null) {
      envVars.put(TESTRUN_ID_KEY, info.getId());
      envVars.put(TESTRUN_HREF_KEY, info.getHref());
      logger.println("Dynatrace: registered test run for system profile " +
              systemProfile + ", version " + dynatraceVersion.getVersionAsString() + " - ID: " +
              info.getId() + " (available in the environment as " + TESTRUN_ID_KEY+")");
    }
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
