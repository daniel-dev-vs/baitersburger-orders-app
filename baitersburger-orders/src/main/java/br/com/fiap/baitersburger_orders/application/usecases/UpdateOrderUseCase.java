package br.com.fiap.baitersburger_orders.application.usecases;

import br.com.fiap.baitersburger_orders.domain.entities.Order;

public interface UpdateOrderUseCase {
    Order updateOrderStatus(String orderId, String status);
}
