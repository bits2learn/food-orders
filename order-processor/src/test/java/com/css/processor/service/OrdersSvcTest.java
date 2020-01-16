package com.css.processor.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;
import com.css.processor.component.Config;
import com.css.processor.component.KitchenShelf;
import com.css.processor.model.Shelf;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class OrdersSvcTest {

  @Mock
  private KitchenShelf kitchenShelf;

  @Mock
  private Config config;

  private OrdersSvc ordersSvc;
  private int totHot = 2;
  private int totCold = 2;
  private int totFrozen = 2;
  private int totOverflow = 4;
  private List<Order> hotOrders = new ArrayList<>();
  private List<Order> coldOrders = new ArrayList<>();
  private List<Order> frozenOrders = new ArrayList<>();
  private List<Order> addedOrders = new ArrayList<>();
  private Shelf hotShelf;
  private Shelf coldShelf;
  private Shelf frozenShelf;
  private Shelf overflowShelf;
  private Counter hot = new Counter();
  private Counter cold = new Counter();
  private Counter frozen = new Counter();
  private Counter overflow = new Counter();
  private boolean moveFromOFToH = false;
  private boolean moveFromOFToC = false;
  private boolean moveFromOFToF = false;

  @Before
  public void setup() throws IOException {
    Mockito.when(config.getHot()).thenReturn(totHot);
    Mockito.when(config.getCold()).thenReturn(totCold);
    Mockito.when(config.getFrozen()).thenReturn(totFrozen);
    Mockito.when(config.getOverflow()).thenReturn(totOverflow);
    Mockito.when(config.getDriverAppUrl()).thenReturn("http://localhost:8090/orderconsumer/driver");
    kitchenShelf = new KitchenShelf(config);
    ordersSvc = new OrdersSvc();
    ordersSvc.setKichenShelf(kitchenShelf);
    hotShelf = kitchenShelf.getShelf(ShelfType.hot);
    coldShelf = kitchenShelf.getShelf(ShelfType.cold);
    frozenShelf = kitchenShelf.getShelf(ShelfType.frozen);
    overflowShelf = kitchenShelf.getShelf(ShelfType.overflow);

    String fileName = getClass().getClassLoader().getResource("orders.json").getFile();
    byte[] jsonData = Files.readAllBytes(Paths.get(fileName));

    ObjectMapper json = new ObjectMapper();
    List<Order> orders = json.readValue(jsonData, new TypeReference<List<Order>>() {});
    orders = orders.stream().distinct().collect(Collectors.toList());

    hotOrders =
        orders.stream().filter(x -> x.getShelfType() == ShelfType.hot).collect(Collectors.toList());
    coldOrders = orders.stream().filter(x -> x.getShelfType() == ShelfType.cold)
        .collect(Collectors.toList());
    frozenOrders = orders.stream().filter(x -> x.getShelfType() == ShelfType.frozen)
        .collect(Collectors.toList());
  }

  @Test
  public void testAddRemoveOrders() {
    testRemoveOrder();
    testAddOrders();
    testRemoveOrder();
  }

  private void testAddOrders() {
    checkKitchenState();

    Order order = hotOrders.stream().findAny().get();
    hotOrders.remove(order);
    addOrder(order);
    hot.incr();
    checkKitchenState();

    order = coldOrders.stream().findAny().get();
    coldOrders.remove(order);
    addOrder(order);
    cold.incr();
    checkKitchenState();

    order = frozenOrders.stream().findAny().get();
    frozenOrders.remove(order);
    addOrder(order);
    frozen.incr();
    checkKitchenState();

    // check for overflow order
    for (int i = 0; i < 2; i++) {
      order = hotOrders.stream().findAny().get();
      hotOrders.remove(order);
      addOrder(order);
      if (hot.getCounter() < totHot) {
        hot.incr();
      } else if (overflow.getCounter() < totOverflow) {
        overflow.incr();
      } else {
        // order wasted
      }
    }
    checkKitchenState();

    for (int i = 0; i < 5; i++) {
      order = coldOrders.stream().findAny().get();
      coldOrders.remove(order);
      addOrder(order);
      if (cold.getCounter() < totCold) {
        cold.incr();
      } else if (overflow.getCounter() < totOverflow) {
        overflow.incr();
      } else {
        // order wasted
      }
    }
    checkKitchenState();
  }

  private void addOrder(Order order) {
    ordersSvc.addOrder(order);
    addedOrders.add(order);
  }

  private void testRemoveOrder() {
    checkKitchenState();

    Optional<Order> orderOpt = addedOrders.stream().findAny();
    if (orderOpt.isPresent()) {
      moveFromOFToH = false;
      moveFromOFToC = false;
      moveFromOFToF = false;
      Order order = orderOpt.get();
      if (hotShelf.getOrders().contains(order)) {
        hot.decr();
        if (hot.getCounter() < totHot) {
          moveFromOFToH = true;
        }
      }
      if (coldShelf.getOrders().contains(order)) {
        cold.decr();
        if (cold.getCounter() < totCold) {
          moveFromOFToC = true;
        }
      }
      if (frozenShelf.getOrders().contains(order)) {
        frozen.decr();
        if (frozen.getCounter() < totFrozen) {
          moveFromOFToF = true;
        }
      }
      if (overflowShelf.getOrders().contains(order)) {
        overflow.decr();
      }
      ordersSvc.removeOrder(order);
      addedOrders.remove(order);
      checkKitchenState();

      checkIfAnyOrderCanBeMoved();
      kitchenShelf.notifyUpdate();
      checkKitchenState();
    }
  }

  /**
   * 
   */
  private void checkIfAnyOrderCanBeMoved() {
    if (moveFromOFToH) {
      overflow.decr();
      hot.incr();
    }
    if (moveFromOFToC) {
      overflow.decr();
      cold.incr();
    }
    if (moveFromOFToF) {
      overflow.decr();
      frozen.incr();
    }
  }

  private void checkKitchenState() {
    assertNotNull(hotShelf);
    assertNotNull(hotShelf.getOrders());
    assertEquals(hot.getCounter(), hotShelf.getOrders().size());

    assertNotNull(coldShelf);
    assertNotNull(coldShelf.getOrders());
    assertEquals(cold.getCounter(), coldShelf.getOrders().size());

    assertNotNull(frozenShelf);
    assertNotNull(frozenShelf.getOrders());
    assertEquals(frozen.getCounter(), frozenShelf.getOrders().size());

    assertNotNull(overflowShelf);
    assertNotNull(overflowShelf.getOrders());
    assertEquals(overflow.getCounter(), overflowShelf.getOrders().size());
  }

  class Counter {
    int counter;

    public void decr() {
      counter--;
    }

    public void incr() {
      counter++;;
    }

    public int getCounter() {
      return counter;
    }

  }

}
