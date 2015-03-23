package com.dynatrace.jenkins.model;

import com.google.gson.annotations.SerializedName;

/**
 * Contains all properties supported by the Dynatrace REST API for registering test runs. Only the build number
 * is mandatory (and populated by Jenkins), the other fields are optional. Used as JSON document for calling
 * the REST API.
 *
 * Created by cwat-wgottesh on 24.02.2015.
 */
public class DynatraceVersion {
  private String category;
  @SerializedName("versionMajor")
  private String major;
  @SerializedName("versionMinor")
  private String minor;
  @SerializedName("versionRevision")
  private String revision;
  @SerializedName("versionMilestone")
  private String milestone;
  @SerializedName("versionBuild")
  private String build;

  public DynatraceVersion() {
  }

  public DynatraceVersion(String category, String major, String minor, String revision, String milestone, int build) {
    this.category = category;
    this.major = major;
    this.minor = minor;
    this.revision = revision;
    this.milestone = milestone;
    this.build = Integer.toString(build);
  }


  public DynatraceVersion(String category, String major, String minor, String revision, String milestone, String build) {
    this.category = category;
    this.major = major;
    this.minor = minor;
    this.revision = revision;
    this.milestone = milestone;
    this.build = build;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
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

  public String getMilestone() {
    return milestone;
  }

  public void setMilestone(String milestone) {
    this.milestone = milestone;
  }

  public String getBuild() {
    return build;
  }

  public void setBuild(String build) {
    this.build = build;
  }

  @Override
  public String toString() {
    return "DynatraceVersion{" +
            ", major='" + major + '\'' +
            ", minor='" + minor + '\'' +
            ", revision='" + revision + '\'' +
            ", milestone='" + milestone + '\'' +
            ", build='" + build + '\'' +
            '}';
  }

  public String getVersionAsString() {
    StringBuilder sb = new StringBuilder();
    if (major != null) sb.append(major).append(".");
    if (minor != null) sb.append(minor).append(".");
    if (revision != null) sb.append(revision).append(".");
    if (milestone != null) sb.append(milestone).append(".");
    sb.append("build ").append(build);
    return sb.toString();
  }
}
