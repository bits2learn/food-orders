package com.css.pc;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;
import com.css.order.common.model.Order;

public class OrderTaskScheduler extends TimerTask {

  private String name;
  private ThreadPoolExecutor producerExecutor;
  private List<Order> orders;
  private String orderUrl;
  private Random random;

  public OrderTaskScheduler(String name, ThreadPoolExecutor producerExecutor, List<Order> orders,
      String orderUrl) {
    this.name = name;
    this.producerExecutor = producerExecutor;
    this.orders = orders;
    this.orderUrl = orderUrl;
    this.random = new Random();
  }

  @Override
  public void run() {
    Order order = orders.get(random.nextInt(orders.size()));
    ProducerTask producerTask = new ProducerTask(name, order, orderUrl);
    producerExecutor.submit(producerTask);
  }

}
