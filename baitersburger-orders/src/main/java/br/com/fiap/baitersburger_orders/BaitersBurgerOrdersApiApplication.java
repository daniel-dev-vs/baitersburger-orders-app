package br.com.fiap.baitersburger_orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.fiap.baitersburger_orders.infra.apis.feign")
public class BaitersBurgerOrdersApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaitersBurgerOrdersApiApplication.class, args);
	}

}
