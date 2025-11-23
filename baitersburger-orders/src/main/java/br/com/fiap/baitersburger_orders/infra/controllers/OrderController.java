package br.com.fiap.baitersburger_orders.infra.controllers;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.usecases.CreateOrderUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.GetOrderUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.impl.CreateOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.GetOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/orders")
public class OrderController {

    CreateOrderUseCase createOrderUseCase;
    GetOrderUseCase getOrderUseCase;

    public OrderController(@Qualifier("OrderDynamoGatewayImpl")OrderGateway gateway) {
        this.createOrderUseCase = new CreateOrderUseCaseImpl(gateway);
        this.getOrderUseCase = new GetOrderUseCaseImpl(gateway);

    }



    @GetMapping("/{orderId}")
    public Object getById(@PathVariable String orderId){
        return ResponseEntity.ok(getOrderUseCase.getById(orderId));
    }

    @GetMapping
    public Object getAll(@RequestParam(required = false) String status){

        if(status != null)
             return ResponseEntity.ok(getOrderUseCase.getByStatus(status));

        return ResponseEntity.ok(getOrderUseCase.getAll());
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody Order requestDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderUseCase.createOrder(requestDto));
    }
}
