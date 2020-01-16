package com.css.processor.model;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;

public class SortOrderByAgeTest {

  @Test
  public void testSortOrderByAge() {
    Date orderDt = new Date();
    List<Order> orders = new ArrayList<>();
    Order o1 = getOrder("Order1", ShelfType.cold, 250, 0.5, orderDt);
    Order o2 = getOrder("Order2", ShelfType.cold, 150, 0.5, orderDt);
    Order o3 = getOrder("Order3", ShelfType.cold, 300, 0.5, orderDt);

    orders.add(o1);
    orders.add(o2);
    orders.add(o3);

    orders.sort(new SortOrderByAge());

    assertEquals(o2, orders.get(0));
    assertEquals(o1, orders.get(1));
    assertEquals(o3, orders.get(2));


  }

  private Order getOrder(String name, ShelfType temp, double shelfLife, double decayRate,
      Date orderDt) {
    Order order = new Order();
    order.setName(name);
    order.setShelfType(temp);
    order.setShelfLife(shelfLife);
    order.setDecayRate(decayRate);
    order.setOrderDt(orderDt);
    return order;
  }

}
