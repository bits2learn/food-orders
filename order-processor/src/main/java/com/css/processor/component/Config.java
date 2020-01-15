package com.css.processor.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "config")
public class Config {
  private int hot;
  private int cold;
  private int frozen;
  private int overflow;
  private String driverAppUrl;

  public int getHot() {
    return hot;
  }

  public void setHot(int hot) {
    this.hot = hot;
  }

  public int getCold() {
    return cold;
  }

  public void setCold(int cold) {
    this.cold = cold;
  }

  public int getFrozen() {
    return frozen;
  }

  public void setFrozen(int frozen) {
    this.frozen = frozen;
  }

  public int getOverflow() {
    return overflow;
  }

  public void setOverflow(int overflow) {
    this.overflow = overflow;
  }

  public String getDriverAppUrl() {
    return driverAppUrl;
  }

  public void setDriverAppUrl(String driverAppUrl) {
    this.driverAppUrl = driverAppUrl;
  }

}
