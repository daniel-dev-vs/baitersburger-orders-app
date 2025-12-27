package br.com.fiap.baitersburger_orders.infra.apis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ProductClient", url = "${PRODUCT_SERVICE_URL}")
public interface ProductClient {

    @GetMapping("/products/{productId}")
    Object getProductById(@PathVariable String productId);
}
