package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.gateways.QRCodeGateway;
import br.com.fiap.baitersburger_orders.application.usecases.CreateOrderUseCase;
import br.com.fiap.baitersburger_orders.domain.entities.Order;

import java.util.List;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    OrderGateway gateway;
    QRCodeGateway qrCodeGateway;

    public CreateOrderUseCaseImpl(OrderGateway gateway, QRCodeGateway qrCodeGateway) {

        this.gateway = gateway;
        this.qrCodeGateway = qrCodeGateway;
    }

    @Override
    public Order createOrder(Order order) {

        Order orderCreated = gateway.createOrder(order);
        orderCreated.setQrCode(qrCodeGateway.createQRCode(orderCreated.getId().toString(),orderCreated.getTotalPrice().toString()));
        gateway.updateOrder(orderCreated);

        return orderCreated;
    }
}
