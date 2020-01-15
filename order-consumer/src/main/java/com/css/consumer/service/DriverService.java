package com.css.consumer.service;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.css.consumer.config.Config;
import com.css.order.common.model.Order;

@Component
public class DriverService {

  @Autowired
  private Config config;

  public void pickupOrder(Order order) {
    Random random = new Random();
    int trafficTime = random.nextInt(config.getMaxDriveTime() + 1) + config.getMinDriveTime();
    Timer timer = new Timer();
    DriverTaskScheduler task = new DriverTaskScheduler(config.getKitchenUrl(), order);
    timer.schedule(task, TimeUnit.SECONDS.toMillis(trafficTime));
  }

}
