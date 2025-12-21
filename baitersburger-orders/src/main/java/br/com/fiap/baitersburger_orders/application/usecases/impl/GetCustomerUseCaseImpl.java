package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.CustomerGateway;
import br.com.fiap.baitersburger_orders.application.usecases.GetCustomerUseCase;
import org.springframework.stereotype.Component;

@Component
public class GetCustomerUseCaseImpl implements GetCustomerUseCase {

    private final CustomerGateway customerGateway;

    public GetCustomerUseCaseImpl(CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    @Override
    public boolean customerExists(String cpf) {
        Object customer = customerGateway.getCustomerByCpf(cpf);
        return customer != null;
    }
}
