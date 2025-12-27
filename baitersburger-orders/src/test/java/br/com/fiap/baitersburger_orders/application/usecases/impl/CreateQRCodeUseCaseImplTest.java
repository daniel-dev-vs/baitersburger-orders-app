package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.QRCodeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreateQRCodeUseCaseImplTest {

    private QRCodeGateway qrCodeGateway;
    private CreateQRCodeUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        qrCodeGateway = mock(QRCodeGateway.class);
        useCase = new CreateQRCodeUseCaseImpl(qrCodeGateway);
    }

    @Test
    void createQRCode_shouldDelegateToGatewayWithAmountAsString() {
        String orderId = "order-123";
        BigDecimal amount = new BigDecimal("100.50");
        String expectedAmountString = "100.50";
        String expectedQrCode = "qr-code-xyz";

        when(qrCodeGateway.createQRCode(orderId, expectedAmountString))
                .thenReturn(expectedQrCode);

        String result = useCase.createQRCode(orderId, amount);

        verify(qrCodeGateway).createQRCode(orderId, expectedAmountString);
        assertEquals(expectedQrCode, result);
    }

    @Test
    void createQRCode_shouldHandleZeroAmount() {
        String orderId = "order-zero";
        BigDecimal amount = BigDecimal.ZERO;
        String expectedAmountString = "0";
        String expectedQrCode = "qr-zero";

        when(qrCodeGateway.createQRCode(orderId, expectedAmountString))
                .thenReturn(expectedQrCode);

        String result = useCase.createQRCode(orderId, amount);

        verify(qrCodeGateway).createQRCode(orderId, expectedAmountString);
        assertEquals(expectedQrCode, result);
    }

    @Test
    void createQRCode_shouldPreserveBigDecimalScaleWhenToString() {
        String orderId = "order-scale";
        BigDecimal amount = new BigDecimal("10.00");
        String expectedAmountString = "10.00";
        String expectedQrCode = "qr-scale";

        when(qrCodeGateway.createQRCode(orderId, expectedAmountString))
                .thenReturn(expectedQrCode);

        String result = useCase.createQRCode(orderId, amount);

        verify(qrCodeGateway).createQRCode(orderId, expectedAmountString);
        assertEquals(expectedQrCode, result);
    }
}