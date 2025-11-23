package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.usecases.GetOrderUseCase;
import br.com.fiap.baitersburger_orders.domain.entities.Order;

import java.util.List;

public class GetOrderUseCaseImpl implements GetOrderUseCase {

    OrderGateway gateway;

    public GetOrderUseCaseImpl(OrderGateway gateway) {
        this.gateway = gateway;
    }
    @Override
    public List<Order> getAll() {
        return gateway.getAll();
    }

    @Override
    public Order getById(String orderId) {
        return gateway.getById(orderId);
    }


    @Override
    public List<Order> getByStatus(String status) {
        return gateway.getByStatus(status);
    }

}
