package com.css.processor.model;

import java.util.concurrent.ArrayBlockingQueue;
import com.css.order.common.model.Order;
import com.css.order.common.model.ShelfType;

public class HotShelf extends Shelf {

  public HotShelf(int size) {
    this.orders = new ArrayBlockingQueue<Order>(size);
    this.size = size;
  }

  @Override
  public ShelfType getShelfType() {
    return ShelfType.hot;
  }

}
