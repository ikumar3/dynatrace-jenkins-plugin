package com.dynatrace.jenkins.dashboard;

import com.dynatrace.jenkins.model.DynatraceConnectionInfo;
import com.dynatrace.jenkins.rest.DynatraceServerEndpoint;
import com.dynatrace.jenkins.rest.DynatraceServerEndpointImpl;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;

/**
 * Created by cwat-wgottesh on 25.02.2015.
 */
public class RestConnectorTest {
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

  @Test
  public void testAccessRestViaHTTPS() {
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
    provider.setCredentials(AuthScope.ANY, credentials);
    RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15*1000)
            .setConnectTimeout(15*1000)
            .build();
    HttpGet httpGet = new HttpGet("https://dynalive.emea.cpwr.corp:8021/rest/management/profiles/dynaTrace%20Monitoring");

    HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(createSocketFactoryRegistry());
    HttpClient httpClient = HttpClients.custom()
            .setConnectionManager(cm)
            .setDefaultCredentialsProvider(provider)
            .setDefaultRequestConfig(requestConfig)
            .build();

    try {
      assertEquals(httpClient.execute(httpGet).getStatusLine().getStatusCode(), 200);
    } catch (IOException e) {
      e.printStackTrace();
    }

//    DynatraceConnectionInfo connectionInfo = new DynatraceConnectionInfo("192.168.56.102", 8021, "admin", "admin");
//    DynatraceServerEndpoint endpoint = new DynatraceServerEndpointImpl(connectionInfo);
//    System.out.println(endpoint.testConnection("easyTravel"));


//    ClientConfig config = new ClientConfig();
//    HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("user", "superSecretPassword");
//    config.register(feature);
//
//    config.connectorProvider(new ApacheConnectorProvider());
//    config.register(createSocketFactoryRegistry());
//
//    JerseyClient client = JerseyClientBuilder.createClient(config);
//    client.target("https://192.168.56.102:8021/rest/management/profiles/abc").request().get();
////    client.target("https://dynalive.emea.cpwr.corp:8021/rest/management/profiles/abc").request().get();
  }
}
