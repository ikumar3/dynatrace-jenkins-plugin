package com.dynatrace.jenkins.model;

import com.dynatrace.jenkins.model.DynatraceVersion;

/**
 * Created by cwat-wgottesh on 26.02.2015.
 */
public class DynatraceTestRunInfo extends DynatraceVersion {
  private String id;
  private String category;
  private String systemProfile;
  private String href;
  private long creationDate;

  public DynatraceTestRunInfo() {
  }

  public String getId() {
    return id;
  }

  public void setId(String testRunID) {
    this.id = testRunID;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public long getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getSystemProfile() {
    return systemProfile;
  }

  public void setSystemProfile(String systemProfile) {
    this.systemProfile = systemProfile;
  }

  @Override
  public String toString() {
    return "DynatraceTestRunInfo{" +
            "id='" + id + '\'' +
            ", category='" + category + '\'' +
            ", systemProfile='" + systemProfile + '\'' +
            ", href='" + href + '\'' +
            ", creationDate=" + creationDate +
            '}';
  }
}
