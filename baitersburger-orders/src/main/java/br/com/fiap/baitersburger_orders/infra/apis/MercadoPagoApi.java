package br.com.fiap.baitersburger_orders.infra.apis;

import br.com.fiap.baitersburger_orders.infra.apis.feign.MercadoPagoClient;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;



@Service
public class MercadoPagoApi {


    private final MercadoPagoClient client;

    @Value("${MERCADO_PAGO_EXTERNAL_POS_ID}")
    private String mercadoPagoExternalPosId;

    @Value("${MERCADO_PAGO_ACCESS_TOKEN}")
    private String mercadoPagadoAccessToken;


    public MercadoPagoApi(MercadoPagoClient client) {
        this.client = client;

    }

    public ResponseQRCodeDTO generateQr(String orderId, String amount) {

        RequestQRCodeDTO requestBody = createDto(orderId, amount);

            return client.createOrderQRCode(
                    UUID.randomUUID().toString(),
                    "Bearer " + mercadoPagadoAccessToken,
                    requestBody);
    }


    public RequestQRCodeDTO createDto(String orderId, String amount) {
        var qr = new QR(mercadoPagoExternalPosId, "dynamic");
        var configQrCode = new ConfigQRCode(qr);
        var payment = new Payment(amount);
        var transaction = new Transaction(List.of(payment));

        return new RequestQRCodeDTO("qr", amount, orderId, configQrCode, transaction);
    }

}
