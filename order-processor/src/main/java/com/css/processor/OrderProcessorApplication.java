package com.css.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.css.processor"})
@SpringBootApplication
public class OrderProcessorApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderProcessorApplication.class, args);
  }

}
