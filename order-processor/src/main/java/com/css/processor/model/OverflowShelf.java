package com.css.processor.model;

import java.util.concurrent.ArrayBlockingQueue;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;

public class OverflowShelf extends Shelf {

  private static final double DECAY_FACTOR = 2;

  public OverflowShelf(int size) {
    this.orders = new ArrayBlockingQueue<Order>(size);
  }

  @Override
  protected double getOrderValue(Order order) {
    long age = getAge(order);
    double orderValue = (order.getShelfLife() - age) - (DECAY_FACTOR * order.getDecayRate() * age);
    return orderValue < 0 ? 0 : orderValue;
  }

  @Override
  protected void moveOverflowShelfOrders(OverflowShelf overflowShelf, int ordersToMove) {
    // no operation, since during rearrangement we only move from other shelves to overflow shelf
    // and not the other way
    return;
  }

  @Override
  public ShelfType getShelfType() {
    return ShelfType.overflow;
  }

}
