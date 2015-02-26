package com.dynatrace.jenkins.rest;

import com.dynatrace.jenkins.model.DynatraceTestRunInfo;
import com.dynatrace.jenkins.model.DynatraceTestRunWrapper;
import com.dynatrace.jenkins.model.DynatraceConnectionInfo;
import com.dynatrace.jenkins.model.DynatraceVersion;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cwat-wgottesh on 24.02.2015.
 */
public class DynatraceServerEndpointImpl implements DynatraceServerEndpoint {

  private static final Logger logger = Logger
          .getLogger(DynatraceServerEndpointImpl.class.getName());
  private static final String PROTOCOL = "https"; // > Dt6.2 only https allowed
  private static final int TIMEOUT = 15000;
  private Registry<ConnectionSocketFactory> socketFactoryRegistry;

  private static final String TEST_CONNECTION = "/rest/management/profiles/%1s";
  private static final String REGISTER_TEST_RUN = "/rest/management/profiles/%1s/testruns.json";

  private String host;
  private int port;
  private String username;
  private String password;


  public DynatraceServerEndpointImpl(String host, String port, String username, String password) {
    this.host = host;
    this.port = Integer.parseInt(port);
    this.username = username;
    this.password = password;
  }

  public DynatraceServerEndpointImpl(DynatraceConnectionInfo connection) {
    this.host = connection.getHost();
    this.port = connection.getPort();
    this.username = connection.getUsername();
    this.password = connection.getPassword();
  }

  private Registry<ConnectionSocketFactory> createSocketFactoryRegistry() {
    SSLContext sslcontext = null;
    try {
      sslcontext = SSLContexts.custom()
              .loadTrustMaterial(null,
                      new TrustStrategy() {

                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                          return true;
                        }
                      })
              .build();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    } catch (KeyStoreException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    HostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
    return RegistryBuilder.<ConnectionSocketFactory> create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier))
            .build();
  }

  private HttpClient getClient() {
    logger.info("Connecting to " + host + " using username \"" + username+"\"");

    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
    provider.setCredentials(AuthScope.ANY, credentials);
    RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(TIMEOUT)
            .setConnectTimeout(TIMEOUT)
            .build();

    if (socketFactoryRegistry == null) {
      socketFactoryRegistry = createSocketFactoryRegistry();
    }
    HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    HttpClient httpClient = HttpClients.custom()
            .setConnectionManager(cm)
            .setDefaultCredentialsProvider(provider)
            .setDefaultRequestConfig(requestConfig)
            .build();

    return httpClient;
  }

  private URI buildURI(String path) {
    URI uri;
    try {
      uri = new URI(PROTOCOL, null, host, port, path, null, null);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return uri;
  }

  public void connect() {
    URI uri = buildURI("");
    HttpClient restClient = getClient();
  }

  @Override
  public DynatraceTestRunInfo registerTestRun(String systemProfile, DynatraceVersion version) {
    DynatraceTestRunInfo testRun = null;
    URI testRunURI = buildURI(String.format(REGISTER_TEST_RUN, systemProfile));
    // build JSON document from version
    Gson gson = new Gson();
    String json = gson.toJson(version);
    HttpClient client = getClient();
    HttpPost post = new HttpPost(testRunURI);
    post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
    try {
      HttpResponse response = client.execute(post);
      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      DynatraceTestRunWrapper data = gson.fromJson(rd, DynatraceTestRunWrapper.class);
      testRun = data.getTestRun();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return testRun;
  }

  /**
   * Tries to access system profile information using the given connection credentials and the specified
   * system profile.
   * @param systemProfile
   * @return TestConnectionResult to indicate success or the cause of the error
   */
  @Override
  public TestConnectionResult testConnection(String systemProfile) {
    HttpClient httpClient = getClient();
    HttpGet systemProfileGet = new HttpGet(buildURI(String.format(TEST_CONNECTION, systemProfile)));
    int responseCode = -1;
    try {
      HttpResponse response = httpClient.execute(systemProfileGet);
      responseCode = response.getStatusLine().getStatusCode();
    } catch (java.net.UnknownHostException e) {
      return TestConnectionResult.UNKNOWN_HOST;
    } catch (org.apache.http.conn.ConnectTimeoutException e) {
      return TestConnectionResult.TIMEOUT;
    } catch (IOException e) {
      logger.log(Level.WARNING, "Error connecting to Dynatrace REST interface", e);
    }

    if (responseCode == 200) {
      return  TestConnectionResult.SUCCESS;
    } else if (responseCode == 404) {
      return TestConnectionResult.SYSTEM_PROFILE_NOT_FOUND;
    } else if (responseCode == 401) {
      return TestConnectionResult.FORBIDDEN;
    } else {
      return TestConnectionResult.OTHER_ERROR;
    }
  }
}

