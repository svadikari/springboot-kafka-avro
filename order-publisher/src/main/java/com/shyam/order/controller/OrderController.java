package com.shyam.order.controller;

import com.shyam.avro.order.Order;
import com.shyam.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author SVadikari on 2/21/21
 */
@RestController

@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(path = "orders")
    public String order(@RequestBody Order orderEvent) {
        orderService.order(orderEvent);
        return "Success";
    }

    @GetMapping
    public String sayHello(@RequestParam String user) {
        return "Hello " + user;
    }
}
