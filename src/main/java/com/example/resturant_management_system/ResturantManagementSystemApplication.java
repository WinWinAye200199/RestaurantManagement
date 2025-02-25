package com.example.resturant_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@EnableScheduling
public class ResturantManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResturantManagementSystemApplication.class, args);
	}

}
