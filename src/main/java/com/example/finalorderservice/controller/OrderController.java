package com.example.finalorderservice.controller;

import com.example.finalorderservice.dto.OrderDto;
import com.example.finalorderservice.jpa.OrderEntity;
import com.example.finalorderservice.service.OrderService;
import com.example.finalorderservice.vo.RequestOrder;
import com.example.finalorderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {
    Environment env;
    OrderService orderService;

    @Autowired
    public OrderController(Environment env, OrderService orderService) {
        this.env = env;
        this.orderService = orderService;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping(value = "/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder orderDetails) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy((MatchingStrategies.STRICT));

        OrderDto orderDto = modelMapper.map(orderDetails,OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto creteDto = orderService.createOrder(orderDto);
        ResponseOrder returnValue = modelMapper.map(creteDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping(value = "/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId,
                                                        @RequestBody RequestOrder orderDetails) {
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
