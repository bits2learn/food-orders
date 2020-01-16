package com.css.order.common.http;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.css.order.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClient {

  private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

  private static final ObjectMapper json = new ObjectMapper();

  public static void sendPost(String url, Order order) {

    try {
      HttpPost post = new HttpPost(url);
      post.addHeader("content-type", "application/json; utf-8");

      post.setEntity(new StringEntity(json.writeValueAsString(order)));
      CloseableHttpClient httpClient = null;

      try {
        httpClient = HttpClients.createDefault();
        httpClient.execute(post);
      } catch (Exception e) {
        LOG.error("Error: " + e);
      } finally {
        if (httpClient != null) {
          httpClient.close();
        }
      }
    } catch (Exception ex) {
      LOG.error("Error: " + ex);
    }
  }
}
