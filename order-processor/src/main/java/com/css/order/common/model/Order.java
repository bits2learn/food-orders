package com.css.order.common.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

  private String name;
  @JsonProperty
  private ShelfType temp;
  private double shelfLife;
  private double decayRate;
  @JsonIgnore
  private Date orderDt;
  private double normalizedValue;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonIgnore
  public ShelfType getShelfType() {
    return temp;
  }

  public void setShelfType(ShelfType temp) {
    this.temp = temp;
  }

  public double getShelfLife() {
    return shelfLife;
  }

  public void setShelfLife(double shelfLife) {
    this.shelfLife = shelfLife;
  }

  public double getDecayRate() {
    return decayRate;
  }

  public void setDecayRate(double decayRate) {
    this.decayRate = decayRate;
  }

  public Date getOrderDt() {
    return orderDt;
  }

  public void setOrderDt(Date orderDt) {
    this.orderDt = orderDt;
  }

  public double getNormalizedValue() {
    return normalizedValue;
  }

  public void setNormalizedValue(double normalizedValue) {
    this.normalizedValue = normalizedValue;
  }

  @Override
  public String toString() {
    StringBuffer sbf = new StringBuffer();
    sbf.append("Order: [").append("name: ").append(name).append(", temp: ").append(temp)
        .append(", shelfLife: ").append(shelfLife).append(", decayRate: ").append(decayRate)
        .append(" ]");
    return sbf.toString();
  }

  public String displayContents() {
    StringBuffer sbf = new StringBuffer();
    sbf.append(name).append(", ").append(temp).append(", ").append(shelfLife).append(", ")
        .append(decayRate).append(", ").append(normalizedValue);
    return sbf.toString();
  }

  @Override
  public boolean equals(Object obj) {
    Order o = (Order) obj;
    return this.getName().equals(o.getName()) && this.getShelfType() == o.getShelfType()
        && this.getShelfLife() == o.getShelfLife() && this.getDecayRate() == o.getDecayRate();

  }

}
