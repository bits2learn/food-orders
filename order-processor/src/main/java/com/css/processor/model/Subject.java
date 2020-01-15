package com.css.processor.model;

import java.util.List;

public interface Subject {

  public void attach(Observer o);

  public void detach(Observer o);

  public void notifyUpdate(OverflowShelf overflowShelf);

  public List<ShelfContents> displayContents(boolean orderAddedOrRemoved);
}
