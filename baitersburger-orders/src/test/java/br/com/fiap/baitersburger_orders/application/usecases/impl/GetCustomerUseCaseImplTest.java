package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.CustomerGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCustomerUseCaseImplTest {

    private CustomerGateway customerGateway;
    private GetCustomerUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        customerGateway = mock(CustomerGateway.class);
        useCase = new GetCustomerUseCaseImpl(customerGateway);
    }

    @Test
    void customerExists_shouldReturnTrue_whenGatewayReturnsNonNull() {
        String cpf = "12345678901";
        Object customerMock = new Object();

        when(customerGateway.getCustomerByCpf(cpf)).thenReturn(customerMock);

        boolean result = useCase.customerExists(cpf);

        assertTrue(result);
        verify(customerGateway).getCustomerByCpf(cpf);
    }

    @Test
    void customerExists_shouldReturnFalse_whenGatewayReturnsNull() {
        String cpf = "12345678901";

        when(customerGateway.getCustomerByCpf(cpf)).thenReturn(null);

        boolean result = useCase.customerExists(cpf);

        assertFalse(result);
        verify(customerGateway).getCustomerByCpf(cpf);
    }

    @Test
    void customerExists_shouldPropagateCpfToGateway_evenWhenNull() {
        when(customerGateway.getCustomerByCpf(null)).thenReturn(null);

        boolean result = useCase.customerExists(null);

        assertFalse(result);
        verify(customerGateway).getCustomerByCpf(null);
    }
}
