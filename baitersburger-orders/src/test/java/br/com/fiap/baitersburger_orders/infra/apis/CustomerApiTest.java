package br.com.fiap.baitersburger_orders.infra.apis;

import br.com.fiap.baitersburger_orders.infra.apis.feign.CustomerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class CustomerApiTest {

    private CustomerClient customerClient;
    private CustomerApi customerApi;

    @BeforeEach
    void setUp() {
        customerClient = mock(CustomerClient.class);
        customerApi = new CustomerApi(customerClient);
    }

    @Test
    void getCustomerByCpf_shouldDelegateToClientAndReturnResult() {
        String cpf = "12345678901";
        Object customerResponse = new Object();

        when(customerClient.getCustomerByCpf(cpf)).thenReturn(customerResponse);

        Object result = customerApi.getCustomerByCpf(cpf);

        assertSame(customerResponse, result);
        verify(customerClient).getCustomerByCpf(cpf);
    }

    @Test
    void getCustomerByCpf_shouldAllowNullCpf() {
        when(customerClient.getCustomerByCpf(null)).thenReturn(null);

        Object result = customerApi.getCustomerByCpf(null);

        assertSame(null, result);
        verify(customerClient).getCustomerByCpf(null);
    }
}
