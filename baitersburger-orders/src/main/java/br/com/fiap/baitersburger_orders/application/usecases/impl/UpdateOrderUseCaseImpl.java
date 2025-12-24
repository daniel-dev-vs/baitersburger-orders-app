package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.usecases.UpdateOrderUseCase;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;


public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {

    private final OrderGateway gateway;

    public UpdateOrderUseCaseImpl(OrderGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Order updateOrderStatus(String orderId, String status) {

            Order order = gateway.getById(orderId);

            if(order == null){
                throw new NotFoundException("Order not found with id: " + orderId);
            }
            order.setStatus(OrderStatus.fromValue(status));
            return gateway.updateOrder(order);
    }
}
