package com.css.app;

import java.io.IOException;
import com.css.pc.OrderProducer;

public class App {

  public static void main(String... args) throws IOException {
    validate(args);
    OrderProducer orderPC = new OrderProducer(args[1]);
    orderPC.init();

  }

  private static void validate(String... args) {
    if (args == null || args.length != 2) {
      wrongUsage();
    } else {
      if (!"-kitchenUrl".equals(args[0]) || !args[1].startsWith("http://")) {
        wrongUsage();
      }
    }
  }

  private static void wrongUsage() {
    System.out.println(
        "Usage:\n\tjava -jar order-producer.jar -kitchenUrl http://localhost:8080/orderprocessor/order");
    System.exit(1);
  }

}
