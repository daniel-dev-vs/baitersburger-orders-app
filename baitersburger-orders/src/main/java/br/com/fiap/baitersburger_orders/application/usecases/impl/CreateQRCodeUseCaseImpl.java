package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.QRCodeGateway;
import br.com.fiap.baitersburger_orders.application.usecases.CreateQRCodeUseCase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreateQRCodeUseCaseImpl implements CreateQRCodeUseCase {

    private final QRCodeGateway qrCodeGateway;

    public CreateQRCodeUseCaseImpl(QRCodeGateway qrCodeGateway) {
        this.qrCodeGateway = qrCodeGateway;
    }

    @Override
    public String createQRCode(String orderId, BigDecimal amount) {
        return qrCodeGateway.createQRCode(orderId, amount.toString());
    }
}
