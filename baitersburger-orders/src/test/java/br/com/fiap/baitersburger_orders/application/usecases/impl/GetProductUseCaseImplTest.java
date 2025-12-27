package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductUseCaseImplTest {

    private ProductGateway productGateway;
    private GetProductUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        productGateway = mock(ProductGateway.class);
        useCase = new GetProductUseCaseImpl(productGateway);
    }

    @Test
    void productsExists_shouldReturnTrue_whenGatewayReturnsNonNull() {
        String productId = "prod-123";
        Object productMock = new Object();

        when(productGateway.getProductById(productId)).thenReturn(productMock);

        boolean result = useCase.productsExists(productId);

        assertTrue(result);
        verify(productGateway).getProductById(productId);
    }

    @Test
    void productsExists_shouldReturnFalse_whenGatewayReturnsNull() {
        String productId = "prod-123";

        when(productGateway.getProductById(productId)).thenReturn(null);

        boolean result = useCase.productsExists(productId);

        assertFalse(result);
        verify(productGateway).getProductById(productId);
    }

    @Test
    void productsExists_shouldPropagateNullIdToGateway() {
        when(productGateway.getProductById(null)).thenReturn(null);

        boolean result = useCase.productsExists(null);

        assertFalse(result);
        verify(productGateway).getProductById(null);
    }
}
