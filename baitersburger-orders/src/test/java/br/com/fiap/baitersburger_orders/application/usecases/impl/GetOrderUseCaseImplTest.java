package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetOrderUseCaseImplTest {

    private OrderGateway gateway;
    private GetOrderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(OrderGateway.class);
        useCase = new GetOrderUseCaseImpl(gateway);
    }

    @Test
    void getAll_shouldReturnListFromGateway() {
        Order order1 = buildOrder(UUID.randomUUID().toString());
        Order order2 = buildOrder(UUID.randomUUID().toString());
        List<Order> expected = List.of(order1, order2);

        when(gateway.getAll()).thenReturn(expected);

        List<Order> result = useCase.getAll();

        assertEquals(expected, result);
        verify(gateway).getAll();
    }

    @Test
    void getById_shouldReturnOrder_whenFound() {
        String id = UUID.randomUUID().toString();
        Order expected = buildOrder(id);

        when(gateway.getById(id)).thenReturn(expected);

        Order result = useCase.getById(id);

        assertSame(expected, result);
        verify(gateway).getById(id);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenGatewayReturnsNull() {
        String id = "non-existing-id";

        when(gateway.getById(id)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.getById(id));

        assertTrue(ex.getMessage().contains(id));
        verify(gateway).getById(id);
    }

    @Test
    void getByStatus_shouldReturnListFromGateway() {
        String status = "REQUESTED";
        List<Order> expected = List.of(buildOrder("1"));

        when(gateway.getByStatus(status)).thenReturn(expected);

        List<Order> result = useCase.getByStatus(status);

        assertEquals(expected, result);
        verify(gateway).getByStatus(status);
    }

    private Order buildOrder(String id) {
        return new Order(
                UUID.fromString(
                        id.matches("^[0-9a-fA-F\\-]{36}$") ? id : UUID.randomUUID().toString()
                ),
                List.of("p1", "p2"),
                new BigDecimal("10.00"),
                LocalDateTime.now(),
                OrderStatus.REQUESTED,
                "12345678901",
                null
        );
    }
}
