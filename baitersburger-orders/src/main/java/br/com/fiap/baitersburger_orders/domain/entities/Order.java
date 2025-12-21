package br.com.fiap.baitersburger_orders.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;
    private List<String> productsId;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String customerId;
    private String qrCode;

    public Order(UUID id, List<String> productsId,
                 BigDecimal totalPrice, LocalDateTime createdAt,
                 OrderStatus status, String customerId, String qrCode) {
        this.id = id;
        this.productsId = productsId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.customerId = customerId;
        this.qrCode = qrCode;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id){
        this.id = id;
    }

    public List<String> getProductsId() {
        return productsId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status){
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String setQrCode(String qrCode) {
        return this.qrCode = qrCode;
    }
}
