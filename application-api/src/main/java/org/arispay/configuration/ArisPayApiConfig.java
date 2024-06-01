package org.arispay.configuration;

import org.arispay.adapters.ClientJpaAdapter;
import org.arispay.adapters.CompanyJpaAdapter;
import org.arispay.adapters.UserJpaAdapter;
import org.arispay.ports.api.ClientServicePort;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.UserServicePort;
import org.arispay.ports.spi.ClientPersistencePort;
import org.arispay.ports.spi.CompanyPersistencePort;
import org.arispay.ports.spi.UserPersistencePort;
import org.arispay.service.ClientServiceImpl;
import org.arispay.service.CompanyServiceImpl;
import org.arispay.service.UserServiceImpl;
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
