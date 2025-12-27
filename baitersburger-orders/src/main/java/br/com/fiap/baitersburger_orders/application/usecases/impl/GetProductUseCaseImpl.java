package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.ProductGateway;
import br.com.fiap.baitersburger_orders.application.usecases.GetProductUseCase;
import org.springframework.stereotype.Component;


@Component
public class GetProductUseCaseImpl implements GetProductUseCase {

    ProductGateway gateway;

    public GetProductUseCaseImpl(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public boolean productsExists(String productId) {
        Object product = gateway.getProductById(productId);

        return product != null;
    }
}
