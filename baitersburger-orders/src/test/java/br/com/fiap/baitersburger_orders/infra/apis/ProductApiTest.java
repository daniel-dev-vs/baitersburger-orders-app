package br.com.fiap.baitersburger_orders.infra.apis;

import br.com.fiap.baitersburger_orders.infra.apis.feign.ProductClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class ProductApiTest {

    private ProductClient productClient;
    private ProductApi productApi;

    @BeforeEach
    void setUp() {
        productClient = mock(ProductClient.class);
        productApi = new ProductApi(productClient);
    }

    @Test
    void getProductById_shouldDelegateToClientAndReturnResult() {
        String productId = "prod-123";
        Object productResponse = new Object();

        when(productClient.getProductById(productId)).thenReturn(productResponse);

        Object result = productApi.getProductById(productId);

        assertSame(productResponse, result);
        verify(productClient).getProductById(productId);
    }

    @Test
    void getProductById_shouldAllowNullId() {
        when(productClient.getProductById(null)).thenReturn(null);

        Object result = productApi.getProductById(null);

        assertSame(null, result);
        verify(productClient).getProductById(null);
    }
}
