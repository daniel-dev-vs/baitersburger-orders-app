package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.infra.apis.feign.ProductClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductGatewayImplTest {

    @Mock
    private ProductClient client;

    @InjectMocks
    private ProductGatewayImpl productGateway;

    @Test
    void shouldReturnProductWhenIdExists() {
        String productId = "prod-123";
        Object expectedProduct = new Object();

        when(client.getProductById(productId)).thenReturn(expectedProduct);

        Object response = productGateway.getProductById(productId);

        assertEquals(expectedProduct, response);
        verify(client, times(1)).getProductById(productId);
    }

    @Test
    void shouldPropagateExceptionWhenClientFails() {
        String productId = "prod-999";
        FeignException.InternalServerError exception = new FeignException.InternalServerError(
                "Internal Server Error",
                mock(Request.class),
                null,
                Map.of()
        );

        when(client.getProductById(productId)).thenThrow(exception);

        assertThrows(FeignException.InternalServerError.class, () -> productGateway.getProductById(productId));
        verify(client, times(1)).getProductById(productId);
    }

    @Test
    void shouldHandleNullResponse() {
        String productId = "empty-id";

        when(client.getProductById(productId)).thenReturn(null);

        Object response = productGateway.getProductById(productId);

        assertEquals(null, response);
        verify(client).getProductById(productId);
    }
}