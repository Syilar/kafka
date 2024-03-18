package com.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderStatusEvent {

    private String status;

    private Instant date;
}
