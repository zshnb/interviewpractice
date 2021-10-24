package com.zshnb.interviewpractice.miaosha;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderInfoController {
    private final OrderInfoService orderInfoService;

    public OrderInfoController(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    @PostMapping("/order-info")
    public String createOrderInfo(@RequestParam String productName) {
        orderInfoService.createOrderInfo(productName);
        return "ok";
    }
}
