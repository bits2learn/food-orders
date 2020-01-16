package com.css.consumer.service;

import static org.mockito.Mockito.times;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.css.consumer.config.Config;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceTest {

  @Mock
  private Config config;

  @Before
  public void setup() {
    Mockito.when(config.getMaxDriveTime()).thenReturn(10);
    Mockito.when(config.getMinDriveTime()).thenReturn(2);
    Mockito.when(config.getKitchenUrl()).thenReturn("http://localhost:8080/orderprocessor/order");
  }

  @Test
  public void testPickUpOrder() {
    DriverService svc = new DriverService();
    svc.setConfig(config);
    svc.pickupOrder(getOrder("Acai Bowl", ShelfType.cold, 249, 0.3));
    Mockito.verify(config, times(1)).getMaxDriveTime();
    Mockito.verify(config, times(1)).getMinDriveTime();
    Mockito.verify(config, times(1)).getKitchenUrl();
  }

  private Order getOrder(String name, ShelfType temp, double shelfLife, double decayRate) {
    Order order = new Order();
    order.setName(name);
    order.setShelfType(temp);
    order.setShelfLife(shelfLife);
    order.setDecayRate(decayRate);
    return order;
  }
}
