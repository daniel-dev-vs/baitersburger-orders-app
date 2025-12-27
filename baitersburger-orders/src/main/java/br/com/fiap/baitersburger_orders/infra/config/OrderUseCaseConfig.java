package br.com.fiap.baitersburger_orders.infra.config;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.gateways.QRCodeGateway;
import br.com.fiap.baitersburger_orders.application.usecases.*;
import br.com.fiap.baitersburger_orders.application.usecases.impl.CreateOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.CreateQRCodeUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.GetOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.UpdateOrderUseCaseImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            OrderGateway gateway,
            CreateQRCodeUseCase createQRCodeUseCase,
            GetCustomerUseCase getCustomerUseCase,
            GetProductUseCase getProductUseCase,
            @Value("${VALIDATE_SERVICE:false}") boolean validate) {
        return new CreateOrderUseCaseImpl(gateway, createQRCodeUseCase, getCustomerUseCase, getProductUseCase, validate);
    }

    @Bean
    public GetOrderUseCase getOrderUseCase(OrderGateway gateway) {
        return new GetOrderUseCaseImpl(gateway);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(OrderGateway gateway) {
        return new UpdateOrderUseCaseImpl(gateway);
    }

    @Bean
    public CreateQRCodeUseCase createQRCodeUseCase(QRCodeGateway gateway) {
        return new CreateQRCodeUseCaseImpl(gateway);
    }

}
