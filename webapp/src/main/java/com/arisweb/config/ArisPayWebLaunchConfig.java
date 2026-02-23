package com.arisweb.config;

import org.arispay.adapters.CompanyAccountJpaAdapter;
import org.arispay.adapters.CompanyJpaAdapter;
import org.arispay.adapters.UserJpaAdapter;
import org.arispay.data.MediaDto;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.FileStorageServicePort;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.ports.api.UserServicePort;
import org.arispay.ports.spi.CompanyPersistencePort;
import org.arispay.ports.spi.FileStorageIOPort;
import org.arispay.ports.spi.UserPersistencePort;
import org.arispay.service.CompanyServiceImpl;
import org.arispay.service.FileStorageServiceImpl;
import org.arispay.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArisPayWebLaunchConfig {

	@Bean
	public UserPersistencePort userPersistence() {
		return new UserJpaAdapter();
	}

	@Bean
	public FileStorageServicePort fileStorageService(FileStorageIOPort fileStorageIO) {
		return new FileStorageServiceImpl(fileStorageIO);
	}

	@Bean
	public UserServicePort userService(FileStorageServicePort fileStorageService, GenericServicePort<MediaDto> mediaService) {
		return new UserServiceImpl(userPersistence(), fileStorageService, mediaService);
	}

	@Bean
	public CompanyPersistencePort companyPersistence() {
		return new CompanyJpaAdapter();
	}

	@Bean
	public CompanyServicePort companyService() {
		return new CompanyServiceImpl(companyPersistence());
	}


	//Company Account Config
	@Bean
	public CompanyAccountJpaAdapter genericPersistenceCompanyAccount() {
		return new CompanyAccountJpaAdapter();
	}

}
