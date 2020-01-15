package com.css.processor.model;

public interface Observer {

  public void update(OverflowShelf overflowShelf);

  public ShelfContents displayContents();

}
