package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.application.gateways.ProductGateway;
import org.springframework.stereotype.Component;

@Component
public class ProductGatewayImpl implements ProductGateway {
    private final br.com.fiap.baitersburger_orders.infra.apis.feign.ProductClient client;

    public ProductGatewayImpl(br.com.fiap.baitersburger_orders.infra.apis.feign.ProductClient client) {
        this.client = client;
    }

    @Override
    public Object getProductById(String productId) {
        return client.getProductById(productId);
    }
}
