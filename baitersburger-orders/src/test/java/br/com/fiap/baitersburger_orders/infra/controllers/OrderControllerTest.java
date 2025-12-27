package br.com.fiap.baitersburger_orders.infra.controllers;

import br.com.fiap.baitersburger_orders.application.usecases.*;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.MercadoPagoDataDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.MercadoPagoRequestDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.order.OrderRequestDto;
import br.com.fiap.baitersburger_orders.infra.dtos.order.RequestUpdateStatusDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.order.ResponseCreateOrderDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.order.ResponseUpdateStatusDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private GetOrderUseCase getOrderUseCase;

    @Mock UpdateOrderUseCase updateOrderUseCase;

    @InjectMocks
    private OrderController orderController;


    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void shouldCreateOrderSuccessfully() {
        // Arrange (Preparação)
        var orderId = UUID.randomUUID();
        var requestDto = new OrderRequestDto(List.of("prod1"),
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                OrderStatus.REQUESTED,
                "12345678901");

        Order mockOrder = mock(Order.class);
        when(mockOrder.getId()).thenReturn(orderId);
        when(mockOrder.getProductsId()).thenReturn(List.of("prod1"));
        when(mockOrder.getTotalPrice()).thenReturn(new BigDecimal("100.00"));
        when(mockOrder.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(mockOrder.getStatus()).thenReturn(OrderStatus.REQUESTED);


        when(createOrderUseCase.createOrder(any(OrderRequestDto.class))).thenReturn(mockOrder);

        ResponseEntity<ResponseCreateOrderDTO> response = orderController.createOrder(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderId.toString(), response.getBody().id());
        verify(createOrderUseCase, times(1)).createOrder(any());
    }

    @Test
    @DisplayName("Deve buscar pedido por ID")
    void shouldGetOrderById() {
        String orderId = UUID.randomUUID().toString();
        Order mockOrder = mock(Order.class);
        when(getOrderUseCase.getById(orderId)).thenReturn(mockOrder);
        ResponseEntity<Object> response = (ResponseEntity<Object>) orderController.getById(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(getOrderUseCase).getById(orderId);
    }


    @Test
    @DisplayName("Deve listar todos os pedidos")
    void shouldGetAllOrders() {
        Order mockOrder = mock(Order.class);
        when(mockOrder.getId()).thenReturn(UUID.randomUUID());
        when(getOrderUseCase.getAll()).thenReturn(List.of(mockOrder));

        ResponseEntity<List<ResponseCreateOrderDTO>> response = orderController.getAll(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        verify(getOrderUseCase).getAll();
    }

    @Test
    void shouldProcessWebhookApprove() {

        var webhookDto = new MercadoPagoRequestDTO(new MercadoPagoDataDTO("order-123", "approved"));

        ResponseEntity<String> response = orderController.approve(Map.of(), webhookDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updateOrderUseCase).updateOrderStatus("order-123", OrderStatus.RECEIVED.toString());
    }

    @Test
    void shouldUpdateOrderStatus() {
        // Arrange
        String orderId = UUID.randomUUID().toString();
        var statusDto = new RequestUpdateStatusDTO("PREPARING");
        Order mockOrder = mock(Order.class);
        when(mockOrder.getId()).thenReturn(UUID.fromString(orderId));

        when(updateOrderUseCase.updateOrderStatus(eq(orderId), eq("PREPARING")))
                .thenReturn(mockOrder);

        ResponseEntity<ResponseUpdateStatusDTO> response = orderController.updateOrderStatus(orderId, statusDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updateOrderUseCase).updateOrderStatus(orderId, "PREPARING");
    }
}