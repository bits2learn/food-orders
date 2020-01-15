package com.css.order.common;

import java.io.IOException;
import org.junit.Test;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestOrderSerDe {
  
  @Test
  public void testOrderSerDe() throws IOException {
    Order order = new Order();
    order.setName("Kale Salad");
    order.setShelfType(ShelfType.cold);
    order.setShelfLife(250);
    order.setDecayRate(0.25);
    System.out.println(order.toString());
    
    ObjectMapper json = new ObjectMapper();
    String orderSer = json.writeValueAsString(order);
    System.out.println(orderSer);
    
    Order orderDe = json.readValue(orderSer, Order.class);
    System.out.println(orderDe.toString());
  }

}
