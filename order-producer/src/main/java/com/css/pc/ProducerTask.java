package com.css.pc;

import com.css.order.common.http.HttpClient;
import com.css.order.common.model.Order;

public class ProducerTask implements Runnable {

  private String name;
  private Order order;
  private String url;

  public ProducerTask(String name, Order order, String url) {
    this.name = name;
    this.order = order;
    this.url = url;
  }

  public void run() {
    System.out.println(name + " -> " + order.toString());
    HttpClient.sendPost(url, order);
  }

}
