package org.arispay.service.fbl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.fbl.BulkPostingResult;
import org.arispay.ports.api.NotificationServicePort;
import org.arispay.ports.spi.CompanyAccountPersistencePort;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

//Todo: Move this to infrastructure module
@Service
public class BulkPostingService {

    private static final Logger logger = LogManager.getLogger(BulkPostingService.class);

    private final BulkTransactionPersistencePort bulkTransactionPersistencePort;
    private final NotificationServicePort notificationService;
    private final CompanyAccountPersistencePort<CompanyAccountDto> companyAccountPersistencePort;

    public BulkPostingService(BulkTransactionPersistencePort bulkTransactionPersistencePort,
                              NotificationServicePort notificationService,
                              @Qualifier("companyAccountJpaAdapter") CompanyAccountPersistencePort<CompanyAccountDto> companyAccountPersistencePort) {
        this.bulkTransactionPersistencePort = bulkTransactionPersistencePort;
        this.notificationService = notificationService;
        this.companyAccountPersistencePort = companyAccountPersistencePort;
    }

    @Scheduled(cron = "*/5 * * * * *")
    private void postTransactions() {
        List<BulkPostingResult> results = bulkTransactionPersistencePort.postTransactions();

        for (BulkPostingResult result : results) {
            try {
                CompanyAccountDto account = companyAccountPersistencePort.getByAccountNumber(result.getAccountDr());
                if (account != null) {
                    notificationService.createBulkPaymentNotification(
                            result.getDetailCount(),
                            result.getTotalAmount(),
                            account.getCompanyId(),
                            "COMPLETED");
                }
            } catch (Exception e) {
                logger.error("Error sending bulk payment completed notification for account {}: {}",
                        result.getAccountDr(), e.getMessage(), e);
            }
        }
    }
}
