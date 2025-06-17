package com.hexaware.maverickBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hexaware.maverickBank"}) 
public class MaverickBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaverickBankApplication.class, args);
	}

}
