package br.com.fiap.baitersburger_orders.infra.controllers;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.gateways.QRCodeGateway;
import br.com.fiap.baitersburger_orders.application.usecases.CreateOrderUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.GetOrderUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.UpdateOrderUseCase;
import br.com.fiap.baitersburger_orders.application.usecases.impl.CreateOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.GetOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.UpdateOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.infra.dtos.order.RequestUpdateStatusDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/orders")
public class OrderController {

    CreateOrderUseCase createOrderUseCase;
    GetOrderUseCase getOrderUseCase;
    UpdateOrderUseCase updateOrderUseCase;

    public OrderController(@Qualifier("OrderDynamoGatewayImpl")OrderGateway gateway, QRCodeGateway qrCodeGateway) {
        this.createOrderUseCase = new CreateOrderUseCaseImpl(gateway,qrCodeGateway);
        this.getOrderUseCase = new GetOrderUseCaseImpl(gateway);
        this.updateOrderUseCase = new UpdateOrderUseCaseImpl(gateway);
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

    @PatchMapping("/{orderId}")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable String orderId, @RequestBody RequestUpdateStatusDTO statusDto){
        return ResponseEntity.ok().body(updateOrderUseCase.updateOrderStatus(orderId, statusDto.status()));
    }
}
