package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    @Value("${app.kafka.orderTopic}")
    private String orderTopic;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Order order) {
        kafkaTemplate.send(orderTopic, order.orderToOrderEvent());

        return ResponseEntity.ok("Message send to kafka");
    }
}
