package com.css.app;

import java.io.File;
import java.io.IOException;
import com.css.producer.OrderProducer;

public class App {

  public static void main(String... args) throws IOException {
    validate(args);
    OrderProducer orderPC = new OrderProducer(args[1], args[3]);
    orderPC.init();

  }

  private static void validate(String... args) {
    if (args == null || args.length != 4) {
      wrongUsage();
    } else {
      if (!"-kitchenUrl".equals(args[0]) || !args[1].startsWith("http://")
          || !"-orders".equals(args[2])) {
        wrongUsage();
      } else {
        File file = new File(args[3]);
        if (!file.exists() || file.isDirectory()) {
          System.out.println("Invalid file");
          wrongUsage();
        }
      }
    }
  }

  private static void wrongUsage() {
    System.out.println(
        "Usage:\n\tjava -jar order-producer.jar -kitchenUrl http://localhost:8080/orderprocessor/order -orders <json_file_path>");
    System.exit(1);
  }

}
