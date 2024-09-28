package com.example.order_processing.service;

public class InventoryResponse {
    private String orderId;
    private boolean success;


    // Constructors
    public InventoryResponse() {
    }

    public InventoryResponse(String orderId, boolean success) {
        this.orderId = orderId;
        this.success = success;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
