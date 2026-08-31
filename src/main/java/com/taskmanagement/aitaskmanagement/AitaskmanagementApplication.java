package com.taskmanagement.aitaskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AitaskmanagementApplication {

	public static void main(String[] args) {

		SpringApplication.run(AitaskmanagementApplication.class, args);
	}

}
