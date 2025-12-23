package br.com.fiap.baitersburger_orders.application.usecases.impl;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.usecases.CreateOrderUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.CreateQRCodeUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.GetCustomerUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.GetProductUseCase;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.infra.dtos.order.OrderRequestDto;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderGateway gateway;
    private final CreateQRCodeUseCase createQrCodeUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final GetProductUseCase getProductUseCase;

    @Value("${VALIDATE_SERVICE:false}")
    private boolean VALIDATE;

    public CreateOrderUseCaseImpl(
            OrderGateway gateway,
            CreateQRCodeUseCase createQRCodeUseCase,
            GetCustomerUseCase getCustomerUseCase,
            GetProductUseCase getProductUseCase) {

        this.gateway = gateway;
        this.createQrCodeUseCase = createQRCodeUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.getProductUseCase = getProductUseCase;
    }

    @Override
    public Order createOrder(OrderRequestDto orderRequestDto) {

        verifyIfCustomerExists(orderRequestDto);
        verifyIfProductsExists(orderRequestDto);

        Order order = new Order(
                orderRequestDto.productsId(),
                orderRequestDto.totalPrice(),
                orderRequestDto.createdAt(),
                orderRequestDto.status(),
                orderRequestDto.customerCpf()
        );


        Order orderCreated = gateway.createOrder(order);
        orderCreated.setQrCode(
                createQrCodeUseCase.createQRCode(
                        orderCreated.getId().toString(),
                        orderCreated.getTotalPrice())
        );

        gateway.updateOrder(orderCreated);

        return orderCreated;
    }

    private void verifyIfProductsExists(OrderRequestDto orderRequestDto) {
        if(VALIDATE) {
            for (String productId : orderRequestDto.productsId()) {
                boolean productExists = getProductUseCase.productsExists(productId);
                if (!productExists) {
                    throw new NotFoundException("Product with ID " + productId + " not found!");
                }
            }
        }
    }

    private void verifyIfCustomerExists(OrderRequestDto orderRequestDto) {
        if (orderRequestDto.customerCpf() != null && VALIDATE) {
            boolean customerExists = getCustomerUseCase.customerExists(orderRequestDto.customerCpf());
            if (!customerExists) {
                throw new NotFoundException("Customer with CPF"+ orderRequestDto.customerCpf() + "not found!");
            }
        }
    }
}
