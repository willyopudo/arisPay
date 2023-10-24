package org.arispay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.arisweb", "org.arispay.configuration", "org.arispay.ports.api", "org.arispay.service", "org.arispay.adapters", "org.arispay.repository", "org.arispay.mappers", "org.arispay.entity"})
public class ArisPayWebLaunchApp {
	public static void main(String[] args) {
		SpringApplication.run(ArisPayWebLaunchApp.class, args);
	}
}