package com.arisweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.arisweb","org.arispay.*"})
@EnableJpaRepositories(basePackages = {"org.arispay.repository"})
@EntityScan(basePackages={"org.arispay.entity"})

public class ArisPayWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArisPayWebApplication.class, args);
	}

}
