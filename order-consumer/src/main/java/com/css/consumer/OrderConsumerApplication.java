package com.css.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@ComponentScan(basePackages = {"com.css.consumer"})
@SpringBootApplication
@EnableAsync
public class OrderConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderConsumerApplication.class, args);
  }
}
