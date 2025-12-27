// java
package br.com.fiap.baitersburger_orders.domain.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void fullConstructor_shouldSetAllFields() {
        UUID id = UUID.randomUUID();
        List<String> productsId = List.of("p1", "p2");
        BigDecimal totalPrice = new BigDecimal("29.90");
        LocalDateTime createdAt = LocalDateTime.now();
        OrderStatus status = OrderStatus.REQUESTED;
        String customerCpf = "12345678901";
        String qrCode = "qr-code-example";

        Order order = new Order(id, productsId, totalPrice, createdAt, status, customerCpf, qrCode);

        assertEquals(id, order.getId());
        assertEquals(productsId, order.getProductsId());
        assertEquals(totalPrice, order.getTotalPrice());
        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(status, order.getStatus());
        assertEquals(customerCpf, order.getCustomerCpf());
        assertEquals(qrCode, order.getQrCode());
    }

    @Test
    void partialConstructor_shouldSetFieldsAndLeaveIdAndQrCodeNull() {
        List<String> productsId = List.of("p1");
        BigDecimal totalPrice = new BigDecimal("10.00");
        LocalDateTime createdAt = LocalDateTime.now();
        OrderStatus status = OrderStatus.RECEIVED;
        String customerCpf = "98765432100";

        Order order = new Order(productsId, totalPrice, createdAt, status, customerCpf);

        assertNull(order.getId());
        assertNull(order.getQrCode());
        assertEquals(productsId, order.getProductsId());
        assertEquals(totalPrice, order.getTotalPrice());
        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(status, order.getStatus());
        assertEquals(customerCpf, order.getCustomerCpf());
    }

    @Test
    void gettersShouldReturnValuesPassedToConstructors() {
        UUID id = UUID.randomUUID();
        List<String> productsId = List.of("a", "b", "c");
        BigDecimal totalPrice = new BigDecimal("100.50");
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        OrderStatus status = OrderStatus.DELIVERED;
        String customerCpf = "11122233344";
        String qrCode = "qr-123";

        Order order = new Order(id, productsId, totalPrice, createdAt, status, customerCpf, qrCode);

        assertAll(
                () -> assertEquals(id, order.getId()),
                () -> assertEquals(productsId, order.getProductsId()),
                () -> assertEquals(totalPrice, order.getTotalPrice()),
                () -> assertEquals(createdAt, order.getCreatedAt()),
                () -> assertEquals(status, order.getStatus()),
                () -> assertEquals(customerCpf, order.getCustomerCpf()),
                () -> assertEquals(qrCode, order.getQrCode())
        );
    }

    @Test
    void setId_shouldUpdateId() {
        Order order = new Order(List.of("p1"), BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        assertNull(order.getId());

        UUID newId = UUID.randomUUID();
        order.setId(newId);

        assertEquals(newId, order.getId());
    }

    @Test
    void setId_shouldAcceptNull() {
        UUID id = UUID.randomUUID();
        Order order = new Order(id, List.of("p1"), BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901", "qr");

        assertNotNull(order.getId());

        order.setId(null);

        assertNull(order.getId());
    }

    @Test
    void setStatus_shouldUpdateStatus() {
        Order order = new Order(List.of("p1"), BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        assertEquals(OrderStatus.REQUESTED, order.getStatus());

        order.setStatus(OrderStatus.PREPARING);

        assertEquals(OrderStatus.PREPARING, order.getStatus());
    }

    @Test
    void setStatus_shouldAcceptNull() {
        Order order = new Order(List.of("p1"), BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        order.setStatus(null);

        assertNull(order.getStatus());
    }

    @Test
    void setQrCode_shouldUpdateQrCode() {
        Order order = new Order(List.of("p1"), BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        assertNull(order.getQrCode());

        String qrCode = "new-qr-code";
        order.setQrCode(qrCode);

        assertEquals(qrCode, order.getQrCode());
    }



    @Test
    void productsId_canBeEmptyList() {
        List<String> productsId = List.of();
        Order order = new Order(productsId, BigDecimal.ZERO, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        assertNotNull(order.getProductsId());
        assertTrue(order.getProductsId().isEmpty());
    }

    @Test
    void productsId_canBeNull() {
        Order order = new Order(null, BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        assertNull(order.getProductsId());
    }

    @Test
    void totalPrice_canBeZero() {
        BigDecimal zero = BigDecimal.ZERO;
        Order order = new Order(List.of("p1"), zero, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        assertEquals(zero, order.getTotalPrice());
    }

    @Test
    void totalPrice_canBeNull() {
        Order order = new Order(List.of("p1"), null, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        assertNull(order.getTotalPrice());
    }

    @Test
    void createdAt_canBeNull() {
        Order order = new Order(List.of("p1"), BigDecimal.ONE, null,
                OrderStatus.REQUESTED, "12345678901");

        assertNull(order.getCreatedAt());
    }

    @Test
    void customerCpf_canBeNullOrEmpty() {
        Order orderNull = new Order(List.of("p1"), BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, null);
        assertNull(orderNull.getCustomerCpf());

        Order orderEmpty = new Order(List.of("p1"), BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "");
        assertEquals("", orderEmpty.getCustomerCpf());
    }

    @Test
    void modifyingExternalProductsListDoesNotAffectOrderReference() {
        List<String> original = new ArrayList<>();
        original.add("p1");

        Order order = new Order(original, BigDecimal.ONE, LocalDateTime.now(),
                OrderStatus.REQUESTED, "12345678901");

        original.add("p2"); // we change original list

        // Order keeps reference to the same list object;
        // this test only checks current behavior (no defensive copy)
        assertEquals(2, order.getProductsId().size());
    }

    @Test
    void gettersShouldNotThrowWhenFieldsAreNull() {
        Order order = new Order(null, null, null, null, null, null, null);

        assertAll(
                () -> assertNull(order.getId()),
                () -> assertNull(order.getProductsId()),
                () -> assertNull(order.getTotalPrice()),
                () -> assertNull(order.getCreatedAt()),
                () -> assertNull(order.getStatus()),
                () -> assertNull(order.getCustomerCpf()),
                () -> assertNull(order.getQrCode())
        );
    }
}
