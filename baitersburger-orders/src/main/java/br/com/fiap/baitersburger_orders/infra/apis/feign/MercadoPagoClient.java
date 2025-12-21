package br.com.fiap.baitersburger_orders.infra.apis.feign;


import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.RequestQRCodeDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.ResponseQRCodeDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "mercadoPagoClient", url = "${MERCADO_PAGO_URL}")
public interface MercadoPagoClient {



    @PostMapping("/orders")

    ResponseQRCodeDTO createOrderQRCode(
            @RequestHeader("X-Idempotency-Key") String idempotency,
            @RequestHeader("Authorization") String authorization,
            @RequestBody RequestQRCodeDTO dto);
}

