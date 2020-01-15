package com.css.consumer.service;

import java.util.TimerTask;
import com.css.order.common.http.HttpClient;
import com.css.order.common.model.Order;

public class DriverTaskScheduler extends TimerTask {

  private String url;
  private Order order;

  public DriverTaskScheduler(String url, Order order) {
    this.url = url;
    this.order = order;
  }

  @Override
  public void run() {
    HttpClient.sendPost(url, order);
  }

}
