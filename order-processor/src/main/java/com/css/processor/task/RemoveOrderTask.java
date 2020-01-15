package com.css.processor.task;

import com.css.order.common.model.Order;
import com.css.processor.service.OrdersSvc;

public class RemoveOrderTask implements Runnable {

  private OrdersSvc ordersSvc;
  private Order order;

  public RemoveOrderTask(OrdersSvc ordersSvc, Order order) {
    this.ordersSvc = ordersSvc;
    this.order = order;
  }

  public void run() {
    ordersSvc.removeOrder(order);
  }
}
