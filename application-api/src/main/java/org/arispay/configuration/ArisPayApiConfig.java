package org.arispay.configuration;

import org.arispay.adapters.*;
import org.arispay.data.CompanyAccountDto;
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

	//Company Account Config
	@Bean
	public CompanyAccountPersistencePort<CompanyAccountDto> companyAccountPersistence() {
		return new CompanyAccountJpaAdapter();
	}

	@Bean
	public CompanyAccountServicePort<CompanyAccountDto> companyAccountService() {
		return new CompanyAccountServiceImpl<CompanyAccountDto>(companyAccountPersistence());
	}

	//User Config
	@Bean
	public UserPersistencePort userPersistence() {
		return new UserJpaAdapter();
	}

	@Bean
	public UserServicePort userService() {
		return new UserServiceImpl(userPersistence());
	}

	//Transaction Config
	@Bean
	public TransactionPersistencePort transactionPersistence() {
		return new TransactionJpaAdapter();
	}

	@Bean
	public TransactionServicePort transactionService(ActivityServicePort activityService) {
		return new TransactionServiceImpl(transactionPersistence(), activityService);
	}

	//Transaction Config
	@Bean
	public TransactionRejectedPersistencePort rejectedTransactionPersistence() {
		return new TransactionRejectedJpaAdapter();
	}

	@Bean
	public TransactionRejectedServicePort rejectedTransactionService() {
		return new TransactionRejectedServiceImpl(rejectedTransactionPersistence());
	}

	@Bean
	public BankPersistencePort bankPersistence() {
		return new BankJpaAdapter();
	}

	@Bean
	public BankServicePort bankService() {
		return new BankServiceImpl(bankPersistence());
	}

	//Dashboard Config
	@Bean
	public DashboardPersistencePort dashboardPersistence() {
		return new DashboardJpaAdapter();
	}

	@Bean
	public DashboardServicePort dashboardService() {
		return new DashboardServiceImpl(dashboardPersistence());
	}

	//Activity Config
	@Bean
	public ActivityPersistencePort activityPersistence() {
		return new ActivityJpaAdapter();
	}

	@Bean
	public ActivityServicePort activityService(org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate) {
		return new ActivityServiceImpl(activityPersistence(), messagingTemplate);
	}

	//Notification Config
	@Bean
	public NotificationPersistencePort notificationPersistence() {
		return new NotificationJpaAdapter();
	}

	@Bean
	public NotificationBroadcastPort notificationBroadcast(org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate) {
		return new WebSocketNotificationBroadcaster(messagingTemplate);
	}

	@Bean
	public NotificationServicePort notificationService(NotificationBroadcastPort notificationBroadcastPort) {
		return new NotificationServiceImpl(notificationPersistence(), notificationBroadcastPort);
	}

}
