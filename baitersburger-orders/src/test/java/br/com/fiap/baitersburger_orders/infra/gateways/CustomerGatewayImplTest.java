package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.infra.apis.feign.CustomerClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.DisplayName;
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
class CustomerGatewayImplTest {

    @Mock
    private CustomerClient client;

    @InjectMocks
    private CustomerGatewayImpl customerGateway;

    @Test
    void shouldReturnCustomerWhenCpfExists() {
        String cpf = "12345678901";
        Object expectedResponse = new Object();

        when(client.getCustomerByCpf(cpf)).thenReturn(expectedResponse);

        Object response = customerGateway.getCustomerByCpf(cpf);

        assertEquals(expectedResponse, response);
        verify(client, times(1)).getCustomerByCpf(cpf);
    }

    @Test
    void shouldPropagateFeignExceptionWhenClientFails() {
        String cpf = "12345678901";

        FeignException.NotFound notFoundException = new FeignException.NotFound(
                "Not Found",
                mock(Request.class),
                null,
                Map.of()
        );

        when(client.getCustomerByCpf(cpf)).thenThrow(notFoundException);

        assertThrows(FeignException.NotFound.class, () -> customerGateway.getCustomerByCpf(cpf));
        verify(client, times(1)).getCustomerByCpf(cpf);
    }

    @Test
    void shouldHandleNullResponseFromClient() {
        String cpf = "00000000000";

        when(client.getCustomerByCpf(cpf)).thenReturn(null);

        Object response = customerGateway.getCustomerByCpf(cpf);

        assertEquals(null, response);
        verify(client).getCustomerByCpf(cpf);
    }
}