package br.com.fiap.baitersburger_orders.application.usecases;

public interface GetCustomerUseCase {
    boolean customerExists(String customerId);
}
