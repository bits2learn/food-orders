package com.css.consumer.service;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.css.consumer.config.Config;
import com.css.order.common.model.Order;

@Component
public class DriverService {

  private static final Logger LOG = LoggerFactory.getLogger(DriverService.class);

  @Autowired
  private Config config;

  public void pickupOrder(Order order) {
    Random random = new Random();
    if (config.getMinDriveTime() > config.getMaxDriveTime()) {
      LOG.error("Configuration error. 'minDriveTime' should be less than 'maxDriveTime'");
      System.exit(-1);
    }
    int trafficTime = random.nextInt(config.getMaxDriveTime() + 1) + config.getMinDriveTime();
    Timer timer = new Timer();
    DriverTaskScheduler task = new DriverTaskScheduler(config.getKitchenUrl(), order);
    timer.schedule(task, TimeUnit.SECONDS.toMillis(trafficTime));
  }

  // VisibleForTesting
  protected void setConfig(Config config) {
    this.config = config;
  }

}
