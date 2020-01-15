package com.css.processor.task;

import com.css.order.common.http.HttpClient;
import com.css.order.common.model.Order;

public class DispatchOrderTask implements Runnable {

  private Order order;
  private String url;

  public DispatchOrderTask(String url, Order order) {
    this.url = url;
    this.order = order;
  }

  public void run() {
    HttpClient.sendPost(url, order);
  }

}
