package com.example.order_processing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
public class Order {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private Long customerId;
    @Getter
    @Setter
    private Long productId;
    @Getter
    @Setter
    private int quantity;
    @Getter
    @Setter
    private BigDecimal totalAmount;
    @Getter
    @Setter
    private String status;
}
