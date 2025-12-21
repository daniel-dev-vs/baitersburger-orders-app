package br.com.fiap.baitersburger_orders.application.gateways;

public interface QRCodeGateway {
    String createQRCode(String orderId, String amount);
}
