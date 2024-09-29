package com.example.order_processing.service;

import com.example.order_processing.common.OrderProcessingException;
import com.example.order_processing.model.Order;
import com.example.order_processing.model.OrderEvent;
import com.example.order_processing.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public Order createOrder(Order order) {
        try {
            Order savedOrder = orderRepository.save(order);

            // Send the order event to Kafka

            CompletableFuture<SendResult<String, OrderEvent>> future = kafkaTemplate.send("order-topic", new OrderEvent(savedOrder));

            // Handle success or failure using CompletableFuture
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    System.err.println("Failed to send order event to Kafka: " + ex.getMessage());
                    // Handle failure (e.g., trigger compensation)
                } else {
                    System.out.println("Sent order event to Kafka: " + result.getProducerRecord().value());
                    // Handle success (e.g., proceed with next steps)
                }
            });

            return savedOrder;
        } catch (Exception e) {
            System.err.println("Error during order processing: " + e.getMessage());
            e.printStackTrace();
            throw new OrderProcessingException("Failed to create order", e);
        }
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
