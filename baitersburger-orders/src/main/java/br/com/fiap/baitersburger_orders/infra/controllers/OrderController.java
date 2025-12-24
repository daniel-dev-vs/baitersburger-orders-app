package br.com.fiap.baitersburger_orders.infra.controllers;

import br.com.fiap.baitersburger_orders.application.gateways.OrderGateway;
import br.com.fiap.baitersburger_orders.application.gateways.QRCodeGateway;
import br.com.fiap.baitersburger_orders.application.usecases.*;
import br.com.fiap.baitersburger_orders.application.usecases.impl.CreateOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.GetOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.application.usecases.impl.UpdateOrderUseCaseImpl;
import br.com.fiap.baitersburger_orders.domain.entities.Order;
import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;
import br.com.fiap.baitersburger_orders.infra.dtos.mercadopago.MercadoPagoRequestDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.order.OrderRequestDto;
import br.com.fiap.baitersburger_orders.infra.dtos.order.RequestUpdateStatusDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.order.ResponseCreateOrderDTO;
import br.com.fiap.baitersburger_orders.infra.dtos.order.ResponseUpdateStatusDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    CreateOrderUseCase createOrderUseCase;
    GetOrderUseCase getOrderUseCase;
    UpdateOrderUseCase updateOrderUseCase;


    public OrderController(
            @Qualifier("OrderDynamoGatewayImpl")OrderGateway gateway,
            CreateQRCodeUseCase createQRCodeUseCase,
            GetCustomerUseCase getCustomerUseCase,
            GetProductUseCase getProductUseCase,
            @Value("${VALIDATE_SERVICE:false}") boolean validate){
        this.createOrderUseCase = new CreateOrderUseCaseImpl(gateway,
                createQRCodeUseCase,
                getCustomerUseCase,
                getProductUseCase,
                validate);
        this.getOrderUseCase = new GetOrderUseCaseImpl(gateway);
        this.updateOrderUseCase = new UpdateOrderUseCaseImpl(gateway);
    }



    @GetMapping("/{orderId}")
    public Object getById(@PathVariable String orderId){
        return ResponseEntity.ok(getOrderUseCase.getById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<ResponseCreateOrderDTO>> getAll(@RequestParam(required = false) String status){

        if(status != null)
            return ResponseEntity.ok(toResponseCreateOrderDtoList(getOrderUseCase.getByStatus(status)));

        return ResponseEntity.ok(toResponseCreateOrderDtoList(getOrderUseCase.getAll()));
    }

    @PostMapping
    public ResponseEntity<ResponseCreateOrderDTO> createOrder(@RequestBody OrderRequestDto requestDto){

        Order order =  createOrderUseCase.createOrder(requestDto);
        ResponseCreateOrderDTO dto = new ResponseCreateOrderDTO(
                order.getId().toString(),
                order.getProductsId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getCustomerCpf(),
                order.getQrCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<ResponseUpdateStatusDTO> updateOrderStatus(@PathVariable String orderId, @RequestBody RequestUpdateStatusDTO statusDto){
        Order order =  updateOrderUseCase.updateOrderStatus(orderId, statusDto.status());
        ResponseUpdateStatusDTO dto = new ResponseUpdateStatusDTO(
                order.getId().toString(),
                order.getProductsId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getCustomerCpf(),
                order.getQrCode());

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/webhooks")
    public ResponseEntity<String> approve(@RequestHeader Map<String, String> headers, @RequestBody MercadoPagoRequestDTO dto) {

        updateOrderUseCase.updateOrderStatus(dto.data().externalReference(),
                            OrderStatus.RECEIVED.toString());

        return ResponseEntity.ok().build();
    }


    private List<ResponseCreateOrderDTO> toResponseCreateOrderDtoList(List<Order> orders){
        return orders.stream().map(o ->
                new ResponseCreateOrderDTO(
                        o.getId().toString(),
                        o.getProductsId(),
                        o.getTotalPrice(),
                        o.getCreatedAt(),
                        o.getStatus(),
                        o.getCustomerCpf(),
                        o.getQrCode()
                )
        ).toList();
    }
}
