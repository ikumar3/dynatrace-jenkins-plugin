package com.dynatrace.jenkins.model;

/**
 * This class encapsulates the connection information required to access the Dynatrace server
 * REST interface. As versions >= 6.2 only allow HTTPS connections, the protocol is not configurable
 * as it will be always HTTPS.
 *
 * Created by cwat-wgottesh on 24.02.2015.
 */
public class DynatraceConnectionInfo {
  // basic dynatrace connection info
  private String host;
  private Integer port;

  private String username;
  private String password;

  public DynatraceConnectionInfo(String host, Integer port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
  }


  public DynatraceConnectionInfo(String host, String port, String username, String password) {
    this.host = host;
    this.port = Integer.parseInt(port);
    this.username = username;
    this.password = password;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
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
}
