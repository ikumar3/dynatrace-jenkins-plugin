package com.dynatrace.jenkins.rest;

import com.dynatrace.jenkins.model.DynatraceConnectionInfo;
import com.dynatrace.jenkins.model.DynatraceVersion;
import com.dynatrace.jenkins.model.TestCategory;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class DynatraceServerEndpointImplTest {

  @Test
  public void testTestConnection() {
    DynatraceConnectionInfo connectionInfo = new DynatraceConnectionInfo("192.168.56.102", 8021, "admin", "admin");
    DynatraceServerEndpoint endpoint = new DynatraceServerEndpointImpl(connectionInfo);

    assertThat(endpoint.testConnection("easyTravel"), equalTo(TestConnectionResult.SUCCESS));
  }

  @Test
  public void testNonExistingServer() {
    DynatraceConnectionInfo connectionInfo = new DynatraceConnectionInfo("IDONTEXIST.emea.cpwr.corp", 8021, "admin", "admin");
    DynatraceServerEndpoint endpoint = new DynatraceServerEndpointImpl(connectionInfo);

    TestConnectionResult result = endpoint.testConnection("easyTravel");
    assertThat(result, equalTo(TestConnectionResult.UNKNOWN_HOST));
  }

  @Test
  public void testUnknownSystemProfile() {
    DynatraceConnectionInfo connectionInfo = new DynatraceConnectionInfo("192.168.56.102", 8021, "admin", "admin");
    DynatraceServerEndpoint endpoint = new DynatraceServerEndpointImpl(connectionInfo);

    TestConnectionResult result = endpoint.testConnection("missing system profile");
    assertThat(result, equalTo(TestConnectionResult.SYSTEM_PROFILE_NOT_FOUND));
  }

  @Test
  public void testWrongPwd() {
    DynatraceConnectionInfo connectionInfo = new DynatraceConnectionInfo("192.168.56.102", 8021, "admin", "iamawrongpassword");
    DynatraceServerEndpoint endpoint = new DynatraceServerEndpointImpl(connectionInfo);

    TestConnectionResult result = endpoint.testConnection("missing system profile");
    assertThat(result, equalTo(TestConnectionResult.FORBIDDEN));
  }


  @Test
  public void testNameInSystemProfile() {
    DynatraceConnectionInfo connectionInfo = new DynatraceConnectionInfo("dynalive.emea.cpwr.corp", 8021, "viewerDev", "labpass");
    DynatraceServerEndpoint endpoint = new DynatraceServerEndpointImpl(connectionInfo);

    TestConnectionResult result = endpoint.testConnection("dynaTrace Monitoring");
    assertThat(result, equalTo(TestConnectionResult.SUCCESS));
  }

  @Test
  public void testRegisterTestRun() {
    DynatraceVersion version = new DynatraceVersion(TestCategory.UNIT, "1", "2","3", "4", "5");
    DynatraceConnectionInfo connectionInfo = new DynatraceConnectionInfo("192.168.56.102", 8021, "admin", "admin");
    DynatraceServerEndpoint endpoint = new DynatraceServerEndpointImpl(connectionInfo);

    endpoint.registerTestRun("easyTravel", version);
  }
}