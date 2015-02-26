package com.dynatrace.jenkins.model;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class DynatraceVersionTest {

  @Test
  public void testVersionToJson()  {
    String expectedJson = "{\"category\":\"unit\",\"versionMajor\":\"1\",\"versionMinor\":\"2\",\"versionRevision\":\"3\",\"versionMilestone\":\"4\",\"versionBuild\":\"5\"}";
    DynatraceVersion version = new DynatraceVersion(TestCategory.UNIT, "1", "2","3", "4", "5");
    Gson gson = new Gson();
    Assert.assertEquals(gson.toJson(version), expectedJson);
  }
}