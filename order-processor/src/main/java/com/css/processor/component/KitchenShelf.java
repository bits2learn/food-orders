package com.css.processor.component;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.css.order.common.model.ShelfType;
import com.css.processor.model.ColdShelf;
import com.css.processor.model.FrozenShelf;
import com.css.processor.model.HotShelf;
import com.css.processor.model.OverflowShelf;
import com.css.processor.model.Shelf;
import com.css.processor.model.ShelfContents;
import com.css.processor.service.OrderPublisher;

@Component
public class KitchenShelf {

  private HotShelf hotShelf;
  private ColdShelf coldShelf;
  private FrozenShelf frozenShelf;
  private OverflowShelf overflowShelf;

  private boolean updateOnRemove;

  private final OrderPublisher publisher = new OrderPublisher();

  @Autowired
  public KitchenShelf(Config config) {
    hotShelf = new HotShelf(config.getHot());
    publisher.attach(hotShelf);
    coldShelf = new ColdShelf(config.getCold());
    publisher.attach(coldShelf);
    frozenShelf = new FrozenShelf(config.getFrozen());
    publisher.attach(frozenShelf);
    overflowShelf = new OverflowShelf(config.getOverflow());
    publisher.attach(overflowShelf);
    updateOnRemove = config.isUpdateOnRemove();
  }

  public Shelf getShelf(ShelfType type) {
    switch (type) {
      case hot:
        return hotShelf;
      case cold:
        return coldShelf;
      case frozen:
        return frozenShelf;
      default:
        return overflowShelf;
    }
  }

  public void notifyUpdate() {
    publisher.notifyUpdate(overflowShelf);
  }

  public List<ShelfContents> displayContents(boolean orderAddedOrRemoved) {
    return publisher.displayContents(orderAddedOrRemoved);
  }

  public boolean updateOnRemove() {
    return updateOnRemove;
  }

}
