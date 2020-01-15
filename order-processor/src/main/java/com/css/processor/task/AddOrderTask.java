package com.css.processor.task;

import com.css.order.common.model.Order;
import com.css.processor.service.OrdersSvc;

public class AddOrderTask implements Runnable {

  private OrdersSvc ordersSvc;
  private Order order;

  public AddOrderTask(OrdersSvc ordersSvc, Order order) {
    this.ordersSvc = ordersSvc;
    this.order = order;
  }

  public void run() {
    ordersSvc.addOrder(order);
  }
}
