package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.usecases.CreateQRCodeUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.GetCustomerUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.GetProductUseCase;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import br.com.fiap.baitersburger_orders.infra.dtos.order.OrderRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseImplTest {

    private OrderGateway gateway;
    private CreateQRCodeUseCase createQRCodeUseCase;
    private GetCustomerUseCase getCustomerUseCase;
    private GetProductUseCase getProductUseCase;

    @BeforeEach
    void setUp() {
        gateway = mock(OrderGateway.class);
        createQRCodeUseCase = mock(CreateQRCodeUseCase.class);
        getCustomerUseCase = mock(GetCustomerUseCase.class);
        getProductUseCase = mock(GetProductUseCase.class);
    }

    private OrderRequestDto buildRequest() {
        return new OrderRequestDto(
                List.of("prod-1", "prod-2"),
                new BigDecimal("100.00"),
                LocalDateTime.of(2024, 1, 1, 10, 0),
                OrderStatus.REQUESTED,
                "12345678901"
        );
    }

    @Test
    void createOrder_shouldCreateAndReturnOrder_whenCustomerAndProductsExist() {
        OrderRequestDto request = buildRequest();

        when(getCustomerUseCase.customerExists("12345678901")).thenReturn(true);
        when(getProductUseCase.productsExists("prod-1")).thenReturn(true);
        when(getProductUseCase.productsExists("prod-2")).thenReturn(true);

        Order created = new Order(
                UUID.randomUUID(),
                request.productsId(),
                request.totalPrice(),
                request.createdAt(),
                request.status(),
                request.customerCpf(),
                null
        );
        when(gateway.createOrder(any(Order.class))).thenReturn(created);

        when(createQRCodeUseCase.createQRCode(created.getId().toString(), created.getTotalPrice()))
                .thenReturn("qr-code-generated");

        CreateOrderUseCaseImpl useCase = new CreateOrderUseCaseImpl(
                gateway,
                createQRCodeUseCase,
                getCustomerUseCase,
                getProductUseCase,
                true
        );

        Order result = useCase.createOrder(request);

        // verify validations
        verify(getCustomerUseCase).customerExists("12345678901");
        verify(getProductUseCase).productsExists("prod-1");
        verify(getProductUseCase).productsExists("prod-2");

        ArgumentCaptor<Order> createCaptor = ArgumentCaptor.forClass(Order.class);
        verify(gateway).createOrder(createCaptor.capture());
        Order orderPassedToCreate = createCaptor.getValue();
        assertEquals(request.productsId(), orderPassedToCreate.getProductsId());
        assertEquals(request.totalPrice(), orderPassedToCreate.getTotalPrice());
        assertEquals(request.createdAt(), orderPassedToCreate.getCreatedAt());
        assertEquals(request.status(), orderPassedToCreate.getStatus());
        assertEquals(request.customerCpf(), orderPassedToCreate.getCustomerCpf());
        assertNull(orderPassedToCreate.getQrCode());

        ArgumentCaptor<Order> updateCaptor = ArgumentCaptor.forClass(Order.class);
        verify(gateway).updateOrder(updateCaptor.capture());
        Order orderPassedToUpdate = updateCaptor.getValue();
        assertEquals("qr-code-generated", orderPassedToUpdate.getQrCode());

        verify(createQRCodeUseCase).createQRCode(created.getId().toString(), created.getTotalPrice());

        assertSame(created, result);
        assertEquals("qr-code-generated", result.getQrCode());
    }

    @Test
    void createOrder_shouldThrowNotFound_whenCustomerDoesNotExist() {
        OrderRequestDto request = buildRequest();

        when(getCustomerUseCase.customerExists("12345678901")).thenReturn(false);

        CreateOrderUseCaseImpl useCase = new CreateOrderUseCaseImpl(
                gateway,
                createQRCodeUseCase,
                getCustomerUseCase,
                getProductUseCase,
                true
        );

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.createOrder(request));

        assertTrue(ex.getMessage().contains("Customer with CPF"));
        verifyNoInteractions(getProductUseCase);
        verifyNoInteractions(gateway);
        verifyNoInteractions(createQRCodeUseCase);
    }

    @Test
    void createOrder_shouldThrowNotFound_whenAnyProductDoesNotExist() {
        OrderRequestDto request = buildRequest();

        when(getCustomerUseCase.customerExists("12345678901")).thenReturn(true);
        when(getProductUseCase.productsExists("prod-1")).thenReturn(true);
        when(getProductUseCase.productsExists("prod-2")).thenReturn(false);

        CreateOrderUseCaseImpl useCase = new CreateOrderUseCaseImpl(
                gateway,
                createQRCodeUseCase,
                getCustomerUseCase,
                getProductUseCase,
                true
        );

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> useCase.createOrder(request));

        assertTrue(ex.getMessage().contains("Product with ID prod-2"));
        verify(gateway, never()).createOrder(any());
        verifyNoInteractions(createQRCodeUseCase);
    }

    @Test
    void createOrder_shouldSkipValidations_whenValidateIsFalse() {
        OrderRequestDto request = buildRequest();

        Order created = new Order(
                UUID.randomUUID(),
                request.productsId(),
                request.totalPrice(),
                request.createdAt(),
                request.status(),
                request.customerCpf(),
                null
        );
        when(gateway.createOrder(any(Order.class))).thenReturn(created);
        when(createQRCodeUseCase.createQRCode(anyString(), any())).thenReturn("qr-no-validate");

        CreateOrderUseCaseImpl useCase = new CreateOrderUseCaseImpl(
                gateway,
                createQRCodeUseCase,
                getCustomerUseCase,
                getProductUseCase,
                false
        );

        Order result = useCase.createOrder(request);

        verifyNoInteractions(getCustomerUseCase);
        verifyNoInteractions(getProductUseCase);
        verify(gateway).createOrder(any(Order.class));
        verify(gateway).updateOrder(any(Order.class));
        assertEquals("qr-no-validate", result.getQrCode());
    }

    @Test
    void createOrder_shouldNotValidateCustomer_whenCpfIsNull() {
        OrderRequestDto request = new OrderRequestDto(
                List.of("prod-1"),
                new BigDecimal("10.00"),
                LocalDateTime.now(),
                OrderStatus.REQUESTED,
                null
        );

        when(getProductUseCase.productsExists("prod-1")).thenReturn(true);

        Order created = new Order(
                UUID.randomUUID(),
                request.productsId(),
                request.totalPrice(),
                request.createdAt(),
                request.status(),
                null,
                null
        );
        when(gateway.createOrder(any(Order.class))).thenReturn(created);
        when(createQRCodeUseCase.createQRCode(anyString(), any())).thenReturn("qr");

        CreateOrderUseCaseImpl useCase = new CreateOrderUseCaseImpl(
                gateway,
                createQRCodeUseCase,
                getCustomerUseCase,
                getProductUseCase,
                true
        );

        Order result = useCase.createOrder(request);

        verifyNoInteractions(getCustomerUseCase);
        verify(getProductUseCase).productsExists("prod-1");
        assertEquals("qr", result.getQrCode());
    }
}