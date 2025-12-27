
package br.com.fiap.baitersburger_orders.domain.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void getValue_shouldReturnConfiguredValue() {
        assertEquals("REQUESTED", OrderStatus.REQUESTED.getValue());
        assertEquals("RECEIVED", OrderStatus.RECEIVED.getValue());
        assertEquals("PREPARING", OrderStatus.PREPARING.getValue());
        assertEquals("READY", OrderStatus.READY.getValue());
        assertEquals("DELIVERED", OrderStatus.DELIVERED.getValue());
    }

    @Test
    void fromValue_shouldReturnEnum_whenExactMatch() {
        assertEquals(OrderStatus.REQUESTED, OrderStatus.fromValue("REQUESTED"));
        assertEquals(OrderStatus.RECEIVED, OrderStatus.fromValue("RECEIVED"));
        assertEquals(OrderStatus.PREPARING, OrderStatus.fromValue("PREPARING"));
        assertEquals(OrderStatus.READY, OrderStatus.fromValue("READY"));
        assertEquals(OrderStatus.DELIVERED, OrderStatus.fromValue("DELIVERED"));
    }

    @Test
    void fromValue_shouldReturnEnum_whenCaseInsensitiveMatch() {
        assertEquals(OrderStatus.REQUESTED, OrderStatus.fromValue("requested"));
        assertEquals(OrderStatus.RECEIVED, OrderStatus.fromValue("rEcEiVeD"));
    }

    @Test
    void fromValue_shouldThrowException_whenInvalidValue() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OrderStatus.fromValue("INVALID")
        );
        assertTrue(ex.getMessage().contains("Order status does not exist"));
    }

    @Test
    void fromValue_shouldThrowException_whenNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> OrderStatus.fromValue(null)
        );
        assertTrue(ex.getMessage().contains("Order status does not exist"));
    }
}
