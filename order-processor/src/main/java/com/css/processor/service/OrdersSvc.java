package com.css.processor.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;
import com.css.processor.component.KitchenShelf;
import com.css.processor.model.Shelf;
import com.css.processor.model.ShelfContents;

@Component
public class OrdersSvc {

  private static final Logger LOG = LoggerFactory.getLogger(OrdersSvc.class);

  @Autowired
  private KitchenShelf kitchenShelf;

  /**
   * This method is used to add the order to the kitchen shelf
   * 
   * @param order
   * @return
   */
  public void addOrder(Order order) {
    Date currDt = new Date();
    order.setOrderDt(currDt);
    Shelf shelf = kitchenShelf.getShelf(order.getShelfType());
    boolean orderAddedToShelf = false;

    // update contents of shelf to see if any order can be moved from overflow shelf
    kitchenShelf.notifyUpdate();

    if (shelf.hasCapacity()) {
      orderAddedToShelf = shelf.addOrder(order);
    }

    if (!orderAddedToShelf) {
      // try adding to overflow shelf
      LOG.trace("order {} is being added to Overflow shelf", order.toString());
      orderAddedToShelf = kitchenShelf.getShelf(ShelfType.overflow).addOrder(order);
    } else {
      LOG.trace("order {} is added to the shelf: ", order.toString());
    }

    if (!orderAddedToShelf) {
      LOG.trace("order {} is wasted since all the shelves are full", order.toString());
    }
    kitchenShelf.displayContents(orderAddedToShelf);
  }

  /**
   * This method is used to remove the specific order from the kitchen shelf
   * 
   * @param order
   * @return
   */
  public void removeOrder(Order order) {
    Shelf shelf = kitchenShelf.getShelf(order.getShelfType());
    boolean orderRemovedFromShelf = false;

    // update contents of shelf to see if any order can be moved from overflow shelf
    kitchenShelf.notifyUpdate();

    BlockingQueue<Order> orders = shelf.getOrders();
    if (orders.contains(order)) {
      orderRemovedFromShelf = orders.remove(order);
    }
    if (orderRemovedFromShelf) {
      LOG.trace("Order is removed from the shelf: {}", order.toString());
    } else {
      // check if order is present on overflow shelf
      orders = kitchenShelf.getShelf(ShelfType.overflow).getOrders();
      if (orders.contains(order)) {
        orderRemovedFromShelf = orders.remove(order);
      }
      if (orderRemovedFromShelf) {
        LOG.trace("Order is removed from the shelf: {}", order.toString());
      } else {
        LOG.trace("Order is not present on the shelf: {}", order.toString());
      }
    }
    kitchenShelf.displayContents(orderRemovedFromShelf);
  }

  /**
   * This method is only for internal API testing purpose only
   * 
   * @return List<ShelfContents>
   */
  public List<ShelfContents> updateKitchenShelfAndDisplayContents() {
    kitchenShelf.notifyUpdate();
    return kitchenShelf.displayContents(true);
  }

  // VisibleForTesting
  protected void setKichenShelf(KitchenShelf kitchenShelf) {
    this.kitchenShelf = kitchenShelf;
  }
}
