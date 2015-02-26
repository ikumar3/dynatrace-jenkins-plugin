package com.dynatrace.jenkins.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynatraceVersionTest {

  @Test
  public void testVersionToJson()  {
    DynatraceVersion version = new DynatraceVersion("1", "2","3", "4", "5");
    Gson gson = new Gson();
    System.out.println(gson.toJson(version));
  }
}