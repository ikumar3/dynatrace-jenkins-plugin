package com.dynatrace.jenkins.dashboard;

import com.dynatrace.jenkins.dashboard.model.json.TestRun;
import com.dynatrace.jenkins.dashboard.model.json.TestRunWrapper;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.*;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by cwat-wgottesh on 23.02.2015.
 */
public class ParseJsonReportTest {
  @Test
  public void testLoadJsonReport() {
    String sampleJsonReport = "two_tests.json";

    try {
      InputStream is = getClass().getResourceAsStream("/" + sampleJsonReport);
      Gson gson = new Gson();
      TestRunWrapper testRunWrapper = gson.fromJson(new BufferedReader(new InputStreamReader(is, "UTF-8")), TestRunWrapper.class);
      assertNotNull(testRunWrapper);
      TestRun testRun = testRunWrapper.getTestRun();
      assertNotNull(testRun);
      assertEquals(testRun.getCategory(), "performance");
      assertEquals(testRun.getId(), "c58bd738-cfc0-4f20-80a4-459eff96385b");
      // check if the version attributes are correct
      assertEquals(testRun.getVersionBuild(), "3");
      assertEquals(testRun.getVersionMajor(), "1");
      assertEquals(testRun.getVersionMilestone(), "Milestone 2");
      assertEquals(testRun.getVersionMinor(), "0");
      assertEquals(testRun.getVersionRevision(), "9");
      assertEquals(testRun.getSystemProfile(), "easyTravel");

      assertNotNull(testRun.getTestResults());
      assertEquals(testRun.getTestResults().size(), 2);

      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testConversion() {
    String s = "/rest/management/profiles/%1s/testruns.json";
    System.out.println(String.format(s, "abl"));
  }
}
