package br.com.fiap.baitersburger_orders.infra.apis;

import br.com.fiap.baitersburger_orders.infra.apis.feign.CustomerClient;
import org.springframework.stereotype.Service;

@Service
public class CustomerApi {

    private final CustomerClient client;

    public CustomerApi(CustomerClient client) {
        this.client = client;
    }

    public Object getCustomerByCpf(String cpf) {
        return client.getCustomerByCpf(cpf);
    }
}
