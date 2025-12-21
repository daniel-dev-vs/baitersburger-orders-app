package br.com.fiap.baitersburger_orders.application.usecases;

import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.infra.dtos.order.OrderRequestDto;

import java.util.List;

public interface CreateOrderUseCase {

    Order createOrder(OrderRequestDto order);
}
