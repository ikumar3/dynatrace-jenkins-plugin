package com.dynatrace.jenkins.builders;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernameListBoxModel;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.dynatrace.jenkins.model.DynatraceConnectionInfo;
import com.dynatrace.jenkins.model.DynatraceVersion;
import com.dynatrace.jenkins.rest.DynatraceServerEndpoint;
import com.dynatrace.jenkins.rest.DynatraceServerEndpointImpl;
import com.dynatrace.jenkins.rest.TestConnectionResult;
import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cwat-wgottesh on 23.02.2015.
 */
public class CreateTestRunBuilder extends Builder {

  @Extension
  public static class Descriptor extends BuildStepDescriptor<Builder> {
    @Override
    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
      return true; // always allowed regardless of project type
    }

    @Override
    public String getDisplayName() {
      return DISPLAY_NAME;
    }

    public ListBoxModel doFillCategoryItems() {
      ListBoxModel model = new ListBoxModel();
      model.add("Unit");
      model.add("Performance");
      model.add("UI Driven");
      model.add("Web API");
      return model;
    }

    public static ListBoxModel doFillCredsItems() {
      StandardUsernameListBoxModel model = new StandardUsernameListBoxModel();

      Item item = Stapler.getCurrentRequest().findAncestorObject(Item.class);

      List<StandardUsernameCredentials> listOfAllCredentials = CredentialsProvider.lookupCredentials(
              StandardUsernameCredentials.class, item, ACL.SYSTEM, Collections.<DomainRequirement> emptyList());

      List<StandardUsernameCredentials> listOfPasswordCredentials = new ArrayList<StandardUsernameCredentials>();

      // we can only use UsernamePasswordCredentials objects
      for (StandardUsernameCredentials c : listOfAllCredentials) {
        if (c instanceof UsernamePasswordCredentials) {
          listOfPasswordCredentials.add(c);
        }
      }
      model.withAll(listOfPasswordCredentials);

      return model;
    }

    @Override
    public CreateTestRunBuilder newInstance(StaplerRequest req, JSONObject formData) throws FormException {
      return new CreateTestRunBuilder(formData);
    }

    public FormValidation doTestDynatraceConnectionUserPass(
            @QueryParameter("host") final String host,
            @QueryParameter("port") final String port,
            @QueryParameter("username") final String username,
            @QueryParameter("password") final String password,
            @QueryParameter("systemProfile") final String systemProfile) {
      FormValidation validationResult;

      DynatraceServerEndpoint server = new DynatraceServerEndpointImpl(host, port, username, password);
      TestConnectionResult result = server.testConnection(systemProfile);
      if (result == TestConnectionResult.SUCCESS) {
        validationResult = FormValidation.ok("Connection successful");
      } else if (result == TestConnectionResult.SYSTEM_PROFILE_NOT_FOUND) {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface succeeded, but system profile " + systemProfile + " could not be found");
      } else if (result == TestConnectionResult.UNKNOWN_HOST) {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface failed, because the host " + host + " could not be found");
      } else if (result == TestConnectionResult.FORBIDDEN) {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface failed, because the provided username/password combination is wrong or does not have permissions to use the REST API");
      } else if (result == TestConnectionResult.TIMEOUT) {
        validationResult = FormValidation.warning("Connection to " + host + " timed out");
      } else {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface failed");
      }

      return validationResult;
    }


    public FormValidation doTestDynatraceConnectionCredentials(
            @QueryParameter("host") final String host,
            @QueryParameter("port") final String port,
            @QueryParameter("creds") final String creds,
            @QueryParameter("systemProfile") final String systemProfile) {
      FormValidation validationResult;

      StandardUsernameCredentials myCredentials = null;
      Item item = null;

      List<StandardUsernameCredentials> credentialsList = CredentialsProvider.lookupCredentials(
              StandardUsernameCredentials.class,
              item,
              ACL.SYSTEM);
      for (StandardUsernameCredentials c : credentialsList) {
        if (creds.equals(c.getId())) {
          myCredentials = c;
          break;
        }
      }

      DynatraceServerEndpoint server = new DynatraceServerEndpointImpl(host, port, myCredentials.getUsername(), ((UsernamePasswordCredentials)myCredentials).getPassword().getPlainText());
      TestConnectionResult result = server.testConnection(systemProfile);
      if (result == TestConnectionResult.SUCCESS) {
        validationResult = FormValidation.ok("Connection successful");
      } else if (result == TestConnectionResult.SYSTEM_PROFILE_NOT_FOUND) {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface succeeded, but system profile " + systemProfile + " could not be found");
      } else if (result == TestConnectionResult.UNKNOWN_HOST) {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface failed, because the host " + host + " could not be found");
      } else if (result == TestConnectionResult.FORBIDDEN) {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface failed, because the provided username/password combination is wrong or does not have permissions to use the REST API");
      } else if (result == TestConnectionResult.TIMEOUT) {
        validationResult = FormValidation.warning("Connection to " + host + " timed out");
      } else {
        validationResult = FormValidation.warning("Connection with Dynatrace REST interface failed");
      }

      return validationResult;
    }
  }

  public static final String USER_PASS = "username";
  public static final String CREDENTIALS_PLUGIN = "credentialsPlugin";
  public static final String DISPLAY_NAME = "Register Testrun with Dynatrace";

  // basic dynatrace connection info
  private String host;
  private String port;

  private String category;
  private String major;
  private String minor;
  private String milestone;
  private String revision;
  private String systemProfile;

  // in case of password auth
  private String username;
  private String password;

  // in case of using the credentials plugin
  private String creds;

  private String authType = "";

  @DataBoundConstructor
  public CreateTestRunBuilder(JSONObject formData) {
    JSONObject authMode = new JSONObject();
    // we deal with a variable number of parameters and stapler doesn't seem to like multiple constructor
    if (formData.has("authenticationMode")) {
      authMode = (JSONObject) formData.get("authenticationMode");
    }

    this.authType = (String) authMode.get("value");
    this.username = (String) authMode.get("username");
    this.password = (String) authMode.get("password");
    this.creds = (String) authMode.get("creds");

    this.host = (String) formData.get("host");
    this.port = (String) formData.get("port");
    this.systemProfile = (String) formData.get("systemProfile");
    this.major = (String) formData.get("major");
    this.minor = (String) formData.get("minor");
    this.milestone = (String) formData.get("milestone");
    this.revision = (String) formData.get("revision");
    this.category = (String) formData.get("category");
  }


  @Override
  public hudson.model.Descriptor<Builder> getDescriptor() {
    return super.getDescriptor();
  }

  private UsernamePasswordCredentials  getCredentials() {
    String credentialId = this.getCreds();
    StandardUsernameCredentials myCredentials = null;
    Item item = null;

    List<StandardUsernameCredentials> credentialsList = CredentialsProvider.lookupCredentials(
            StandardUsernameCredentials.class,
            item,
            ACL.SYSTEM);
    for (StandardUsernameCredentials c : credentialsList) {
      if (credentialId.equals(c.getId())) {
        myCredentials = c;
        break;
      }
    }
    return (UsernamePasswordCredentials) myCredentials;
  }

  /**
   * Decides based on the authType if the password entered in the form properties or the credentials are used.
   * @return the password for accessing the Dynatrace REST API
   */
  private String getDynatraceUsername() {
    String authType = this.getAuthType();
    String myUsername = null;

    if (authType.equals(USER_PASS)) {
      myUsername = username;
    } else if (authType.equals(CREDENTIALS_PLUGIN)) {
      myUsername = this.getCredentials().getUsername();
    }

    return myUsername;
  }

  /**
   * Decides based on the authType if the password entered in the form properties or the credentials are used.
   * @return the password for accessing the Dynatrace REST API
   */
  private String getDynatracePassword() {
    String authType = this.getAuthType();
    String myPassword = null;

    if (authType.equals(USER_PASS)) {
      myPassword = password;
    } else if (authType.equals(CREDENTIALS_PLUGIN)) {
      myPassword = Secret.toString(this.getCredentials().getPassword());
    }

    return myPassword;
  }

  @Override
  public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
//    getCredentials();
    PrintStream logger = listener.getLogger();
    System.out.println(this);
    DynatraceVersion version = new DynatraceVersion(category, major, minor, revision, milestone, build.getNumber());
    build.addAction(new TestrunIDContributorAction(
            logger,
            new DynatraceConnectionInfo(host, port, getDynatraceUsername(), getDynatracePassword()),
            systemProfile,
            version));
    build.getEnvironment(listener);

    return true;
  }

  // ---- Getters

  public String getAuthType() {
    return authType;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getMajor() {
    return major;
  }

  public void setMajor(String major) {
    this.major = major;
  }

  public String getMinor() {
    return minor;
  }

  public void setMinor(String minor) {
    this.minor = minor;
  }

  public String getRevision() {
    return revision;
  }

  public void setRevision(String revision) {
    this.revision = revision;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getSystemProfile() {
    return systemProfile;
  }

  public void setSystemProfile(String systemProfile) {
    this.systemProfile = systemProfile;
  }

  public String getMilestone() {
    return milestone;
  }

  public void setMilestone(String milestone) {
    this.milestone = milestone;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCreds() {
    return creds;
  }

  public void setCreds(String creds) {
    this.creds = creds;
  }

  public void setAuthType(String authType) {
    this.authType = authType;
  }

  @Override
  public String toString() {
    return "CreateTestRunBuilder{" +
            "host='" + host + '\'' +
            ", port='" + port + '\'' +
            ", major='" + major + '\'' +
            ", minor='" + minor + '\'' +
            ", milestone='" + milestone + '\'' +
            ", revision='" + revision + '\'' +
            ", systemProfile='" + systemProfile + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", creds='" + creds + '\'' +
            ", authType='" + authType + '\'' +
            '}';
  }
}
