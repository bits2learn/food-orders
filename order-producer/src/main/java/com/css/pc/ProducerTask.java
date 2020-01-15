package com.css.pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.css.order.common.http.HttpClient;
import com.css.order.common.model.Order;

public class ProducerTask implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(ProducerTask.class);
  private Order order;
  private String url;

  public ProducerTask(Order order, String url) {
    this.order = order;
    this.url = url;
  }

  public void run() {
    LOG.info("Producer order: {}", order.toString());
    HttpClient.sendPost(url, order);
  }

}
