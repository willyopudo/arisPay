package org.arispay.configuration;

import org.arispay.adapters.CompanyJpaAdapter;
import org.arispay.adapters.FileStorageLocalDiskAdapter;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.FileStorageServicePort;
import org.arispay.ports.spi.CompanyPersistencePort;
import org.arispay.ports.spi.FileStorageIOPort;
import org.arispay.service.CompanyServiceImpl;
import org.arispay.service.FileStorageServiceImpl;
import org.arispay.service.UserServiceImpl;
import org.arispay.ports.spi.UserPersistencePort;
import org.arispay.adapters.UserJpaAdapter;
import org.arispay.ports.api.UserServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArisPayWebLaunchConfig {

	@Bean
	public UserPersistencePort userPersistence() {
		return new UserJpaAdapter();
	}

	@Bean
	public UserServicePort userService() {
		return new UserServiceImpl(userPersistence());
	}

	@Bean
	public CompanyPersistencePort companyPersistence() {
		return new CompanyJpaAdapter();
	}

	@Bean
	public CompanyServicePort companyService() {
		return new CompanyServiceImpl(companyPersistence());
	}

	@Bean
	public FileStorageIOPort fileStorageIO() {
		return new FileStorageLocalDiskAdapter();
	}

	@Bean
	public FileStorageServicePort fileStorageService() {
		return new FileStorageServiceImpl(fileStorageIO());
	}
}
