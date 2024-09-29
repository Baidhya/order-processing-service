package com.example.order_processing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "\"order\"")
public class Order implements Serializable {
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
