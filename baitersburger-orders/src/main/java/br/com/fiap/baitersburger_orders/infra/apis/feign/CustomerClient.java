package br.com.fiap.baitersburger_orders.infra.apis.feign;


import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.ResponseQRCodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "CustomerClient", url = "${CUSTOMER_SERVICE_URL}")
public interface CustomerClient {

    @GetMapping("/customers/{cpf}")
    ResponseQRCodeDTO getCustomerByCpf(@PathVariable String cpf);
}
