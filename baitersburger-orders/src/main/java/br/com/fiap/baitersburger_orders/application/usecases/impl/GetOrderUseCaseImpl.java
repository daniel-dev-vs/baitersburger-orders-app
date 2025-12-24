package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.usecases.GetOrderUseCase;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import feign.FeignException;

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

        Order order = gateway.getById(orderId);
        if (order == null) {
            throw new NotFoundException("Order not found with id: " + orderId);
        }

        return order;
    }


    @Override
    public List<Order> getByStatus(String status) {
        return gateway.getByStatus(status);
    }

}
