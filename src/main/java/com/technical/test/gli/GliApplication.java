package com.technical.test.gli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.technical.test.gli.entity")
public class GliApplication {

	public static void main(String[] args) {
		SpringApplication.run(GliApplication.class, args);
	}

}
