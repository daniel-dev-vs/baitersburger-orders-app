package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import br.com.fiap.baitersburger_orders.infra.persistence.OrderDynamoEntity;
import br.com.fiap.baitersburger_orders.infra.persistence.OrderDynamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDynamoGatewayImplTest {

    @Mock
    private OrderDynamoRepository repository;

    @InjectMocks
    private OrderDynamoGatewayImpl gateway;

    @Test
    @DisplayName("Deve retornar Order convertida quando encontrar no repositório")
    void shouldReturnOrderWhenIdExists() {
        UUID id = UUID.randomUUID();
        OrderDynamoEntity entity = new OrderDynamoEntity(id, List.of("p1"), BigDecimal.TEN, LocalDateTime.now(), OrderStatus.RECEIVED, "123", "qr");
        when(repository.getById(id.toString())).thenReturn(entity);

        Order result = gateway.getById(id.toString());

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(OrderStatus.RECEIVED, result.getStatus());
    }

    @Test
    void shouldReturnNullWhenIdDoesNotExist() {
        String id = "invalid-id";
        when(repository.getById(id)).thenReturn(null);

        Order result = gateway.getById(id);

        assertNull(result);
    }

    @Test
    void shouldReturnListOfOrdersByStatus() {
        String status = "RECEIVED";
        OrderDynamoEntity entity = new OrderDynamoEntity(UUID.randomUUID(), List.of("p1"), BigDecimal.TEN, LocalDateTime.now(), OrderStatus.RECEIVED, "123", "qr");
        when(repository.getByStatus(status)).thenReturn(List.of(entity));

        List<Order> result = gateway.getByStatus(status);

        assertEquals(1, result.size());
        assertEquals(OrderStatus.RECEIVED, result.get(0).getStatus());
    }

    @Test
    void shouldReturnAllOrders() {
        OrderDynamoEntity entity = new OrderDynamoEntity(UUID.randomUUID(), List.of("p1"), BigDecimal.TEN, LocalDateTime.now(), OrderStatus.DELIVERED, "123", "qr");
        when(repository.getAll()).thenReturn(List.of(entity));

        List<Order> result = gateway.getAll();

        assertEquals(1, result.size());
        assertEquals(OrderStatus.DELIVERED, result.get(0).getStatus());
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        Order order = new Order(null, List.of("p1"), BigDecimal.TEN, LocalDateTime.now(), OrderStatus.REQUESTED, "123", "qr");

        Order result = gateway.createOrder(order);

        assertNotNull(result.getId());
        verify(repository, times(1)).save(any(OrderDynamoEntity.class));
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        UUID id = UUID.randomUUID();
        Order order = new Order(id, List.of("p1"), BigDecimal.TEN, LocalDateTime.now(), OrderStatus.PREPARING, "123", "qr");

        Order result = gateway.updateOrder(order);

        assertEquals(id, result.getId());
        verify(repository, times(1)).updateOrder(argThat(entity -> entity.getOrderId().equals(id)));
    }
}