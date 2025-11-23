package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.usecases.CreateOrderUseCase;
import br.com.fiap.baitersburger_orders.domain.entities.Order;

import java.util.List;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    OrderGateway gateway;

    public CreateOrderUseCaseImpl(OrderGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Order createOrder(Order order) {
        return gateway.createOrder(order);
    }
}
