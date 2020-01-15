package com.css.processor.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Shelf implements Observer {

  private static final Logger LOG = LoggerFactory.getLogger(HotShelf.class);
  private static final ObjectMapper JSON = new ObjectMapper();
  private DecimalFormat df = new DecimalFormat("#.00000");
  protected BlockingQueue<Order> orders;

  public abstract ShelfType getShelfType();

  public BlockingQueue<Order> getOrders() {
    return orders;
  }

  public boolean addOrder(Order order) {
    try {
      return orders.offer(order);
    } catch (NullPointerException e) {
      LOG.error("Error while adding order to {}", this.getClass(), e);
      return false;
    }
  }

  public Order removeOrder() {
    return orders.poll();
  }

  public boolean hasCapacity() {
    return orders.remainingCapacity() > 0;
  }

  @Override
  public void update(OverflowShelf overflowshelf) {
    if (orders != null && !orders.isEmpty()) {
      int orderCntBefore = orders.size();
      List<Order> nonDecayOrders = orders.stream().map(order -> getUpdateOrder(order))
          .filter(order -> order.getNormalizedValue() > 0).collect(Collectors.toList());
      orders.retainAll(nonDecayOrders);
      int orderCntAfter = orders.size();
      moveOverflowShelfOrders(overflowshelf, orderCntBefore - orderCntAfter);
    }
  }

  protected void moveOverflowShelfOrders(OverflowShelf overflowShelf, int ordersToMove) {
    if (ordersToMove > 0) {
      List<Order> overflowOrdersWhichCanBeMoved = overflowShelf.getOrders().stream()
          .filter(order -> order.getShelfType() == this.getShelfType()).sorted(new SortOrderByAge())
          .collect(Collectors.toList());

      if (overflowOrdersWhichCanBeMoved.size() > 0) {
        List<Order> ordersToRemoveFromOverflowShelf = new ArrayList<>();
        for (Order order : overflowOrdersWhichCanBeMoved) {
          if (!orders.offer(order)) {
            break;
          } else {
            ordersToRemoveFromOverflowShelf.add(order);
          }
        }
        overflowShelf.getOrders().removeAll(ordersToRemoveFromOverflowShelf);
      }
    }
  }

  protected Order getUpdateOrder(Order order) {
    double orderValue = getOrderValue(order);
    double normalizedValue = order.getShelfLife() != 0 ? orderValue / order.getShelfLife() : 0.0;
    order.setNormalizedValue(Double.parseDouble(df.format(normalizedValue)));
    return order;
  }

  protected double getOrderValue(Order order) {
    long age = getAge(order);
    double orderValue = (order.getShelfLife() - age) - (order.getDecayRate() * age);
    return orderValue < 0 ? 0.0 : orderValue;
  }

  protected long getAge(Order order) {
    Date currDt = new Date();
    return TimeUnit.MILLISECONDS.toSeconds(currDt.getTime() - order.getOrderDt().getTime());
  }

  @Override
  public ShelfContents displayContents() {
    ShelfContents contents = new ShelfContents();
    contents.setShelfType(this.getShelfType());
    contents.setSize(orders.size());
    contents.setOrders(orders);
    try {
      LOG.info(JSON.writeValueAsString(contents));
    } catch (JsonProcessingException e) {
      LOG.error("Error while printing contents", e);
    }
    return contents;
  }

}
