package com.example.order_processing.service;

import com.example.order_processing.model.Order;
import com.example.order_processing.model.OrderEvent;
import com.example.order_processing.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public Order createOrder(Order order) {
        // Business logic for order creation
        Order savedOrder = orderRepository.save(order);
        // Publish event to Kafka for inventory and payment processing
        kafkaTemplate.send("order-topic", new OrderEvent(savedOrder));
        return savedOrder;
    }

    @KafkaListener(topics = "inventory-response")
    public void handleInventoryResponse(InventoryResponse response) {
        if (response.isSuccess()) {
            // Log success message
            System.out.println("Inventory processed successfully for order ID: " + response.getOrderId());

            // Additional logic foor updating the order status, in case of  a successful inventory response

            updateOrderStatus(response.getOrderId());

        } else {
            // Handle failure response
            System.err.println("Inventory processing failed for order ID: " + response.getOrderId());

            // Compensation logic: Rollback order or take necessary action
            rollbackOrder(response.getOrderId());
        }
    }

    private void rollbackOrder(String orderId) {
        Order order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    private void updateOrderStatus(String orderId) {
        // Logic to update the order status in your database
        Order order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("INVENTORY_CONFIRMED");
        orderRepository.save(order);
    }

    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
