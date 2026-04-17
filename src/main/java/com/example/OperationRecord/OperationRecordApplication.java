package com.example.OperationRecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OperationRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationRecordApplication.class, args);
	}

}
