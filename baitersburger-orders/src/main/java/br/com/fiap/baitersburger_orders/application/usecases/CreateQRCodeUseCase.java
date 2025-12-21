package br.com.fiap.baitersburger_orders.application.usecases;

import java.math.BigDecimal;


public interface CreateQRCodeUseCase {
    String createQRCode(String orderId, BigDecimal amount);
}
