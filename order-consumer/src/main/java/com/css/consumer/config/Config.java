package com.css.consumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "config")
public class Config {

  private String kitchenUrl;
  private int minDriveTime;
  private int maxDriveTime;

  public String getKitchenUrl() {
    return kitchenUrl;
  }

  public void setKitchenUrl(String kitchenUrl) {
    this.kitchenUrl = kitchenUrl;
  }

  public int getMinDriveTime() {
    return minDriveTime;
  }

  public void setMinDriveTime(int minDriveTime) {
    this.minDriveTime = minDriveTime;
  }

  public int getMaxDriveTime() {
    return maxDriveTime;
  }

  public void setMaxDriveTime(int maxDriveTime) {
    this.maxDriveTime = maxDriveTime;
  }

}
