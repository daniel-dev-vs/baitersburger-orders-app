package br.com.fiap.baitersburger_orders.infra.dtos.order;

import br.com.fiap.baitersburger_orders.domain.entities.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record OrderRequestDto(
         List<String> productsId,
         BigDecimal totalPrice,
         LocalDateTime createdAt,
         OrderStatus status,
         String customerCpf
        ) {
}
