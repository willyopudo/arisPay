package org.arispay.configuration;

import org.arispay.adapters.*;
import org.arispay.ports.api.*;
import org.arispay.ports.spi.*;
import org.arispay.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArisPayApiConfig {

	//Company Config
	@Bean
	public CompanyPersistencePort companyPersistence() {
		return new CompanyJpaAdapter();
	}

	@Bean
	public CompanyServicePort companyService() {
		return new CompanyServiceImpl(companyPersistence());
	}

	//Client Config
	@Bean
	public ClientPersistencePort clientPersistence() { return new ClientJpaAdapter(); }

	@Bean
	public ClientServicePort clientService() {return new ClientServiceImpl(clientPersistence()); }

	//User Config
	@Bean
	public UserPersistencePort userPersistence() {
		return new UserJpaAdapter();
	}

	@Bean
	public UserServicePort userService() {
		return new UserServiceImpl(userPersistence());
	}

}
