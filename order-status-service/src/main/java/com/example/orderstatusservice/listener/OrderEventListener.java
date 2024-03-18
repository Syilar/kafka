package com.example.orderstatusservice.listener;

import com.example.orderservice.model.OrderEvent;
import com.example.orderservice.model.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    @Value("${app.kafka.orderStatusTopic}")
    private String orderStatusTopic;

    @KafkaListener(topics = "${app.kafka.orderTopic}",
            groupId = "${app.kafka.orderEventGroup}",
            containerFactory = "orderEventConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload OrderEvent message,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(value = KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(value = KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received message: {}", message);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp: {}", key, partition, topic, timestamp);

        kafkaTemplate.send(orderStatusTopic, new OrderStatusEvent("CREATED", Instant.now()));
    }
}
