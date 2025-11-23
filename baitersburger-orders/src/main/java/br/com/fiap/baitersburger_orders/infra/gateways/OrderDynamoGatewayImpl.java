package br.com.fiap.baitersburger_orders.infra.gateways;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.infra.persistence.OrderDynamoEntity;
import br.com.fiap.baitersburger_orders.infra.persistence.OrderDynamoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component("OrderDynamoGatewayImpl")
public class OrderDynamoGatewayImpl implements OrderGateway {

    private final OrderDynamoRepository repository;

    public OrderDynamoGatewayImpl(OrderDynamoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order getById(String orderId) {
        OrderDynamoEntity orderDynamo = repository.getById(orderId);
        return new  Order(
                orderDynamo.getOrderId(),
                orderDynamo.getProductsId(),
                orderDynamo.getTotalPrice(),
                orderDynamo.getCreatedAt(),
                orderDynamo.getStatus(),
                orderDynamo.getCustomerId(),
                orderDynamo.getQrCode()
        );
    }

    @Override
    public List<Order> getByStatus(String status) {
        return repository.getByStatus(status)
                .stream()
                .map(orderDynamo -> {
                    return new Order(
                            orderDynamo.getOrderId(),
                            orderDynamo.getProductsId(),
                            orderDynamo.getTotalPrice(),
                            orderDynamo.getCreatedAt(),
                            orderDynamo.getStatus(),
                            orderDynamo.getCustomerId(),
                            orderDynamo.getQrCode()
                    );
                }).toList();
    }

    @Override
    public List<Order> getAll() {
        return repository.getAll()
                .stream()
                .map(orderDynamo -> {
                    return new Order(
                            orderDynamo.getOrderId(),
                            orderDynamo.getProductsId(),
                            orderDynamo.getTotalPrice(),
                            orderDynamo.getCreatedAt(),
                            orderDynamo.getStatus(),
                            orderDynamo.getCustomerId(),
                            orderDynamo.getQrCode()
                            );
                }).toList();

    }

    @Override
    public Order createOrder(Order order) {
        OrderDynamoEntity dynamoEntity =
                new OrderDynamoEntity(UUID.randomUUID(),
                        order.getProductsId(),
                        order.getTotalPrice(),
                        order.getCreatedAt(),
                        order.getStatus(),
                        order.getCustomerId(),
                        order.getQrCode());

        repository.save(dynamoEntity);

        order.setId(dynamoEntity.getOrderId());
        return order;


    }

}
