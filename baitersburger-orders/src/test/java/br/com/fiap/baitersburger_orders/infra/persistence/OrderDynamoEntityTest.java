package br.com.fiap.baitersburger_orders.infra.persistence;

import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderDynamoEntityTest {

    @Test
    void shouldValidateFullConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        List<String> products = List.of("p1", "p2");
        BigDecimal price = new BigDecimal("150.50");
        LocalDateTime now = LocalDateTime.now();
        OrderStatus status = OrderStatus.RECEIVED;
        String cpf = "12345678901";
        String qr = "qr-code-data";

        OrderDynamoEntity entity = new OrderDynamoEntity(id, products, price, now, status, cpf, qr);

        assertEquals(id, entity.getOrderId());
        assertEquals(products, entity.getProductsId());
        assertEquals(price, entity.getTotalPrice());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(status, entity.getStatus());
        assertEquals(cpf, entity.getCustomerCpf());
        assertEquals(qr, entity.getQrCode());
    }

    @Test
    void shouldValidateSettersAndDefaultConstructor() {
        OrderDynamoEntity entity = new OrderDynamoEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        List<String> products = List.of("p1");

        entity.setOrderId(id);
        entity.setProductsId(products);
        entity.setTotalPrice(BigDecimal.TEN);
        entity.setCreatedAt(now);
        entity.setStatus(OrderStatus.DELIVERED);
        entity.setCustomerCpf("00011122233");
        entity.setQrCode("base64data");

        assertNotNull(entity);
        assertEquals(id, entity.getOrderId());
        assertEquals(products, entity.getProductsId());
        assertEquals(BigDecimal.TEN, entity.getTotalPrice());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(OrderStatus.DELIVERED, entity.getStatus());
        assertEquals("00011122233", entity.getCustomerCpf());
        assertEquals("base64data", entity.getQrCode());
    }
}