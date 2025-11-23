package br.com.fiap.baitersburger_orders.application.gateways;

import br.com.fiap.baitersburger_orders.domain.entities.Order;

import java.util.List;

public interface OrderGateway {
    Order getById(String orderId);
    List<Order> getByStatus(String status);
    List<Order> getAll();
    Order createOrder(Order order);
}
