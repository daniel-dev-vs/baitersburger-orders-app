package br.com.fiap.baitersburger_orders.infra.apis;

import br.com.fiap.baitersburger_orders.infra.apis.feign.MercadoPagoClient;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
public class MercadoPagoApi {

    private final MercadoPagoClient client;

    @Value("${MERCADO_PAGO_EXTERNAL_POS_ID}")
    private String MERCADO_PAGO_EXTERNAL_POS_ID;

    @Value("${MERCADO_PAGO_ACCESS_TOKEN}")
    private String MERCADO_PAGO_ACCESS_TOKEN;


    public MercadoPagoApi(MercadoPagoClient client) {
        this.client = client;

    }

    public ResponseQRCodeDTO generateQr(String orderId, String amount) {

        RequestQRCodeDTO requestBody = createDto(orderId, amount);
        try{
            ResponseQRCodeDTO response = client.createOrderQRCode(
                    UUID.randomUUID().toString(),
                    "Bearer " + MERCADO_PAGO_ACCESS_TOKEN,
                    requestBody);
            return response;
        }
        catch (Exception ex){
            throw ex;
        }



    }


    public RequestQRCodeDTO createDto(String orderId, String amount) {
        var qr = new QR(MERCADO_PAGO_EXTERNAL_POS_ID, "dynamic");
        var configQrCode = new ConfigQRCode(qr);
        var payment = new Payment(amount);
        var transaction = new Transaction(List.of(payment));

        return new RequestQRCodeDTO("qr", amount, orderId, configQrCode, transaction);
    }

}
