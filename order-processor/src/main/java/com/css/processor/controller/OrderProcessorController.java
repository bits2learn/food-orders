package com.css.processor.controller;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.css.order.common.model.Order;
import com.css.processor.component.Config;
import com.css.processor.model.ShelfContents;
import com.css.processor.service.OrdersSvc;
import com.css.processor.task.AddOrderTask;
import com.css.processor.task.DispatchOrderTask;
import com.css.processor.task.RemoveOrderTask;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class OrderProcessorController {

  @Autowired
  private OrdersSvc ordersSvc;

  @Autowired
  private Config config;

  private BlockingQueue<Runnable> orderBlockingQueue = new ArrayBlockingQueue<Runnable>(100);
  private final ThreadPoolExecutor orderExecutor = new ThreadPoolExecutor(10, 20, 10000,
      TimeUnit.MILLISECONDS, orderBlockingQueue, new ThreadPoolExecutor.DiscardPolicy());

  @RequestMapping(value = "/order", method = RequestMethod.POST)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
  @ApiOperation(value = "Submit order to the kitchen", tags = "order")
  public String submitOrder(@RequestBody Order order) {
    AddOrderTask addOrderTask = new AddOrderTask(ordersSvc, order);
    DispatchOrderTask dispatchOrderTask = new DispatchOrderTask(config.getDriverAppUrl(), order);
    orderExecutor.submit(addOrderTask);
    orderExecutor.submit(dispatchOrderTask);
    return "Successfully submitted order";
  }

  @RequestMapping(value = "/kitchen/displayContents", method = RequestMethod.GET)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
  @ApiOperation(value = " Display contents of kitchen shelves", tags = "contents")
  public List<ShelfContents> displayContents() {
    return ordersSvc.updateKitchenShelfAndDisplayContents();
  }

  @RequestMapping(value = "/order/dispatch", method = RequestMethod.POST)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
  @ApiOperation(value = "Dispatch order from kitchen shelf to the driver", tags = "dispatch")
  public String dispatchOrder(@RequestBody Order order) {
    RemoveOrderTask removeOrderTask = new RemoveOrderTask(ordersSvc, order);
    orderExecutor.submit(removeOrderTask);
    return "Successfully dispatched order";
  }

}
