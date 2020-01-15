package com.css.pc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.css.order.common.model.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderProducer {

  private static final long PERIOD_FOUR_SECS = 4000l;
  private static final long PERIOD_ONE_SECS = 1000l;
  private String orderUrl;

  public OrderProducer(String orderUrl) {
    this.orderUrl = orderUrl;
  }

  public void init() throws IOException {
    BlockingQueue<Runnable> producerBlockingQueue = new ArrayBlockingQueue<Runnable>(4);
    ThreadPoolExecutor producerExecutor = new ThreadPoolExecutor(3, 4, 10000, TimeUnit.MILLISECONDS,
        producerBlockingQueue, new ThreadPoolExecutor.DiscardPolicy());

    byte[] jsonData = Files.readAllBytes(Paths.get("./Engineering_Challenge_-_Orders.json"));

    ObjectMapper json = new ObjectMapper();
    List<Order> orders = json.readValue(jsonData, new TypeReference<List<Order>>() {});
    Timer timer = new Timer();

    for (int i = 1; i <= 4; i++) {
      OrderTaskScheduler task = new OrderTaskScheduler(producerExecutor, orders, orderUrl);
      long period = i == 4 ? PERIOD_FOUR_SECS : PERIOD_ONE_SECS;
      timer.schedule(task, period, period);
    }
  }
}
