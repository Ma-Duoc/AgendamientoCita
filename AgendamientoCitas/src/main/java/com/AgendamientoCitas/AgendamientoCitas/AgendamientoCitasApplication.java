package com.AgendamientoCitas.AgendamientoCitas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AgendamientoCitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendamientoCitasApplication.class, args);
	}

}
