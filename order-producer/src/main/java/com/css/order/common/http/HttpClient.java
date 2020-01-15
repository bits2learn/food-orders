package com.css.order.common.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.css.order.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClient {

  private static final ObjectMapper json = new ObjectMapper();

  public static void sendPost(String url, Order order) {

    try {
      HttpPost post = new HttpPost(url);
      post.addHeader("content-type", "application/json; utf-8");

      post.setEntity(new StringEntity(json.writeValueAsString(order)));
      CloseableHttpClient httpClient = null;

      try {
        httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);
        String respData = EntityUtils.toString(response.getEntity());
        // ignore printing respData
      } catch (Exception e) {
        System.out.println("Error: " + e);
        System.exit(-1);
      } finally {
        if (httpClient != null) {
          httpClient.close();
        }
      }
    } catch (Exception ex) {
      System.out.println("Error: " + ex);
      System.exit(-1);
    }
  }
}
