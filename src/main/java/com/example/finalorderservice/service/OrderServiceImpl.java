package com.example.finalorderservice.service;

import com.example.finalorderservice.dto.OrderDto;
import com.example.finalorderservice.jpa.OrderEntity;
import com.example.finalorderservice.jpa.OrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{

    OrderRepository orderRepository;

    @Autowired //주입하고 싶은것 : orderRepsitiory
    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository; //주입이 완료

    }
    public OrderDto createOrder(OrderDto orderDetails) {
        orderDetails.setOrderId((UUID.randomUUID().toString()));
        orderDetails.setTotalPrice(orderDetails.getQty()*orderDetails.getUnitPrice());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderEntity orderEntity = modelMapper.map(orderDetails, OrderEntity.class);

        orderRepository.save(orderEntity);

        OrderDto returnValue = modelMapper.map(orderEntity, OrderDto.class);
        return returnValue;
    }


    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new ModelMapper().map(orderEntity, OrderDto.class);

        return orderDto;
    }


    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);

    }

 }
