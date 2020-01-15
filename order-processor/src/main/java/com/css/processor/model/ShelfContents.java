package com.css.processor.model;

import java.util.concurrent.BlockingQueue;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;

public class ShelfContents {

  private ShelfType shelfType;
  private int size;
  private Object[] orders;

  public ShelfType getShelfType() {
    return shelfType;
  }

  public void setShelfType(ShelfType shelfType) {
    this.shelfType = shelfType;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Object[] getOrders() {
    return orders;
  }

  public void setOrders(BlockingQueue<Order> orders) {
    this.orders = orders.toArray();
  }

}
