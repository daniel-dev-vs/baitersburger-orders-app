package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateOrderUseCaseImplTest {

    private OrderGateway gateway;
    private UpdateOrderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(OrderGateway.class);
        useCase = new UpdateOrderUseCaseImpl(gateway);
    }

    private Order buildOrder(UUID id, OrderStatus status) {
        return new Order(
                id,
                List.of("prod-1", "prod-2"),
                new BigDecimal("50.00"),
                LocalDateTime.now(),
                status,
                "12345678901",
                null
        );
    }

    @Test
    void update_shouldChangeStatusAndPersist_whenOrderExists() {
        String id = UUID.randomUUID().toString();
        UUID uuid = UUID.fromString(id);
        Order existing = buildOrder(uuid, OrderStatus.REQUESTED);

        when(gateway.getById(id)).thenReturn(existing);
        when(gateway.updateOrder(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = useCase.updateOrderStatus(id, OrderStatus.PREPARING.getValue());

        verify(gateway).getById(id);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(gateway).updateOrder(captor.capture());
        Order updated = captor.getValue();

        assertEquals(OrderStatus.PREPARING, updated.getStatus());
        assertEquals(existing.getId(), updated.getId());
        assertSame(updated, result);
    }

    @Test
    void update_shouldThrowNotFoundException_whenOrderDoesNotExist() {
        String id = UUID.randomUUID().toString();

        when(gateway.getById(id)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> useCase.updateOrderStatus(id, OrderStatus.DELIVERED.getValue())
        );

        assertTrue(ex.getMessage().contains(id));
        verify(gateway).getById(id);
        verify(gateway, never()).updateOrder(any());
    }

    @Test
    void update_shouldAllowSameStatusWithoutError() {
        String id = UUID.randomUUID().toString();
        UUID uuid = UUID.fromString(id);
        Order existing = buildOrder(uuid, OrderStatus.REQUESTED);

        when(gateway.getById(id)).thenReturn(existing);
        when(gateway.updateOrder(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = useCase.updateOrderStatus(id, OrderStatus.REQUESTED.getValue());

        verify(gateway).getById(id);
        verify(gateway).updateOrder(any(Order.class));
        assertEquals(OrderStatus.REQUESTED, result.getStatus());
    }
}
