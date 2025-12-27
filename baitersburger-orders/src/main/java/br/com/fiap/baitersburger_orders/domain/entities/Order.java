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
    private String customerCpf;
    private String qrCode;

    public Order(UUID id, List<String> productsId,
                 BigDecimal totalPrice, LocalDateTime createdAt,
                 OrderStatus status, String customerCpf, String qrCode) {
        this.id = id;
        this.productsId = productsId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.customerCpf = customerCpf;
        this.qrCode = qrCode;
    }

    public Order(List<String> productsId,
                 BigDecimal totalPrice, LocalDateTime createdAt,
                 OrderStatus status, String customerCpf) {
        this.productsId = productsId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.customerCpf = customerCpf;
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

    public String getCustomerCpf() {
        return customerCpf;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
