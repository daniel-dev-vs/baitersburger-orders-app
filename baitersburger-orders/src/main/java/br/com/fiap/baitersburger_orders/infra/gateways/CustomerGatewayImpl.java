package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.application.gateways.CustomerGateway;
import br.com.fiap.baitersburger_orders.infra.apis.feign.CustomerClient;
import org.springframework.stereotype.Component;

@Component
public class CustomerGatewayImpl implements CustomerGateway {

    private final CustomerClient client;

    public CustomerGatewayImpl(CustomerClient client) {
        this.client = client;
    }

    @Override
    public Object getCustomerByCpf(String cpf) {
        return client.getCustomerByCpf(cpf);
    }
}
