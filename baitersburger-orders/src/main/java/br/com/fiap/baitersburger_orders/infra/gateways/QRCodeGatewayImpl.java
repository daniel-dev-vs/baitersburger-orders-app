package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.application.gateways.QRCodeGateway;
import br.com.fiap.baitersburger_orders.infra.apis.MercadoPagoApi;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.ResponseQRCodeDTO;
import org.springframework.stereotype.Component;

@Component
public class QRCodeGatewayImpl implements QRCodeGateway {

    MercadoPagoApi mercadoPagoApi;

    public QRCodeGatewayImpl(MercadoPagoApi mercadoPagoApi) {
        this.mercadoPagoApi = mercadoPagoApi;
    }

    @Override
    public String createQRCode(String orderId, String amount) {
        ResponseQRCodeDTO response = mercadoPagoApi.generateQr(orderId,amount);

        return response.typeResponse().qrData();
    }
}
