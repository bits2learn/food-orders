package com.css.processor.model;

import java.util.Comparator;
import java.util.Date;
import com.css.order.common.model.Order;

public class SortOrderByAge implements Comparator<Order> {

  public int compare(Order a, Order b) {
    double aValue = getOrderValue(a);
    double bValue = getOrderValue(b);
    return aValue < bValue ? -1 : (aValue == bValue) ? 0 : 1;
  }

  private double getOrderValue(Order order) {
    Date currDt = new Date();
    long age = currDt.getTime() - order.getOrderDt().getTime();
    double orderValue = (order.getShelfLife() - age) - (order.getDecayRate() * age);
    return orderValue < 0 ? 0 : orderValue;
  }
}
