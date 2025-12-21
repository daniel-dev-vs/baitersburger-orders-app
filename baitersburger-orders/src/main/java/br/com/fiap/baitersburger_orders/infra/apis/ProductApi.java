package br.com.fiap.baitersburger_orders.infra.apis;

import br.com.fiap.baitersburger_orders.infra.apis.feign.ProductClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ProductApi {

    private final ProductClient client;

    public ProductApi(ProductClient client) {
        this.client = client;
    }

    public Object getProductById(String productId) {
        return client.getProductById(productId);
    }

}
