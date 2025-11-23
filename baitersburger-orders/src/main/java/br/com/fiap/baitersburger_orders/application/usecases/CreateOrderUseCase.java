package br.com.fiap.baitersburger_orders.application.usecases;

import br.com.fiap.baitersburger_orders.domain.entities.Order;

import java.util.List;

public interface CreateOrderUseCase {

    Order createOrder(Order order);
}
