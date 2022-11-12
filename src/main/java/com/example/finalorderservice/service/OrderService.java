package com.example.finalorderservice.service;

import com.example.finalorderservice.dto.OrderDto;
import com.example.finalorderservice.jpa.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
