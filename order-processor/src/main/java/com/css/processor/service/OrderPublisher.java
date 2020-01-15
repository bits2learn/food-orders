package com.css.processor.service;

import java.util.ArrayList;
import java.util.List;
import com.css.processor.model.Observer;
import com.css.processor.model.OverflowShelf;
import com.css.processor.model.ShelfContents;
import com.css.processor.model.Subject;

public class OrderPublisher implements Subject {

  private List<Observer> observers = new ArrayList<>();

  @Override
  public void attach(Observer obs) {
    observers.add(obs);
  }

  @Override
  public void detach(Observer obs) {
    observers.remove(obs);
  }

  @Override
  public void notifyUpdate(OverflowShelf overflowShelf) {
    for (Observer obs : observers) {
      obs.update(overflowShelf);
    }
  }

  @Override
  public List<ShelfContents> displayContents(boolean orderAddedOrRemoved) {
    List<ShelfContents> contents = new ArrayList<>();
    if (orderAddedOrRemoved) {
      for (Observer obs : observers) {
        contents.add(obs.displayContents());
      }
    }
    return contents;
  }

}
