package com.css.consumer.service;

import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.css.order.common.http.HttpClient;
import com.css.order.common.model.Order;

public class DriverTaskScheduler extends TimerTask {

  private static final Logger LOG = LoggerFactory.getLogger(DriverTaskScheduler.class);
  private String url;
  private Order order;

  public DriverTaskScheduler(String url, Order order) {
    this.url = url;
    this.order = order;
  }

  @Override
  public void run() {
    LOG.info("Driver picked up order: {}", order.toString());
    HttpClient.sendPost(url, order);
  }

}
