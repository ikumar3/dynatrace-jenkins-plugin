package com.dynatrace.jenkins.model;

/**
 * Created by cwat-wgottesh on 24.02.2015.
 */
public class DynatraceVersion {
  private String versionMajor;
  private String versionMinor;
  private String versionRevision;
  private String versionMilestone;
  private String versionBuild;

  public DynatraceVersion() {
  }

  public DynatraceVersion(String versionMajor, String versionMinor, String versionRevision, String versionMilestone, int versionBuild) {
    this.versionMajor = versionMajor;
    this.versionMinor = versionMinor;
    this.versionRevision = versionRevision;
    this.versionMilestone = versionMilestone;
    this.versionBuild = Integer.toString(versionBuild);
  }


  public DynatraceVersion(String versionMajor, String versionMinor, String versionRevision, String versionMilestone, String versionBuild) {
    this.versionMajor = versionMajor;
    this.versionMinor = versionMinor;
    this.versionRevision = versionRevision;
    this.versionMilestone = versionMilestone;
    this.versionBuild = versionBuild;
  }

  public String getVersionMajor() {
    return versionMajor;
  }

  public void setVersionMajor(String versionMajor) {
    this.versionMajor = versionMajor;
  }

  public String getVersionMinor() {
    return versionMinor;
  }

  public void setVersionMinor(String versionMinor) {
    this.versionMinor = versionMinor;
  }

  public String getVersionRevision() {
    return versionRevision;
  }

  public void setVersionRevision(String versionRevision) {
    this.versionRevision = versionRevision;
  }

  public String getVersionMilestone() {
    return versionMilestone;
  }

  public void setVersionMilestone(String versionMilestone) {
    this.versionMilestone = versionMilestone;
  }

  public String getVersionBuild() {
    return versionBuild;
  }

  public void setVersionBuild(String versionBuild) {
    this.versionBuild = versionBuild;
  }

  @Override
  public String toString() {
    return "DynatraceVersion{" +
            ", versionMajor='" + versionMajor + '\'' +
            ", versionMinor='" + versionMinor + '\'' +
            ", versionRevision='" + versionRevision + '\'' +
            ", versionMilestone='" + versionMilestone + '\'' +
            ", versionBuild='" + versionBuild + '\'' +
            '}';
  }
}
