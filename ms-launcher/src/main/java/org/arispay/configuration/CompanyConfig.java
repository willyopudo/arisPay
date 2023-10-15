package org.arispay.configuration;

import org.arispay.adapters.CompanyJpaAdapter;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.spi.CompanyPersistencePort;
import org.arispay.service.CompanyServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyConfig {
	@Bean
	public CompanyPersistencePort companyPersistence() {
		return new CompanyJpaAdapter();
	}

	@Bean
	public CompanyServicePort companyService() {
		return new CompanyServiceImpl(companyPersistence());
	}
}
