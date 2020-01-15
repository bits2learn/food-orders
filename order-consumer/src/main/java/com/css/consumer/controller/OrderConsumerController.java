package com.css.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.css.consumer.service.DriverService;
import com.css.order.common.model.Order;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class OrderConsumerController {

  @Autowired
  private DriverService driverService;

  @RequestMapping(value = "/driver", method = RequestMethod.POST)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
  @ApiOperation(value = "Dispatches driver", tags = "driver")
  public String dispatchDriver(@RequestBody Order order) {
    driverService.pickupOrder(order);
    return "Successful";
  }

}
