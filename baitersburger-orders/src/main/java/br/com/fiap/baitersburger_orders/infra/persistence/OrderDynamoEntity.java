package br.com.fiap.baitersburger_orders.infra.persistence;

import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DynamoDbBean
@Component
public class OrderDynamoEntity {

    private UUID orderId;
    private List<String> productsId;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String customerCpf;
    private String qrCode;

    @Autowired
    public OrderDynamoEntity(){}


    public OrderDynamoEntity(UUID orderId, List<String> productsId, BigDecimal totalPrice, LocalDateTime createdAt, OrderStatus status, String customerCpf, String qrCode) {
        this.orderId = orderId;
        this.productsId = productsId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.status = status;
        this.customerCpf = customerCpf;
        this.qrCode = qrCode;
    }

    @DynamoDbPartitionKey
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public List<String> getProductsId() {
        return productsId;
    }

    public void setProductsId(List<String> productsId) {
        this.productsId = productsId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @DynamoDbSecondarySortKey(indexNames = "status-createdAt-index")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "status-createdAt-index")
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCustomerCpf() {
        return customerCpf;
    }

    public void setCustomerCpf(String customerCpf) {
        this.customerCpf = customerCpf;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
