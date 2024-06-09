package com.coldie.kitchenstocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class KitchenStocksApplication {

	public static void main(String[] args) {
		SpringApplication.run(KitchenStocksApplication.class, args);
	}

}
