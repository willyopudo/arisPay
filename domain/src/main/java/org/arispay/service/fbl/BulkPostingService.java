package org.arispay.service.fbl;

import org.arispay.ports.spi.TransactionPersistencePort;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class BulkPostingService {
    private final BulkTransactionPersistencePort bulkTransactionPersistencePort;
    private BulkPostingService(BulkTransactionPersistencePort bulkTransactionPersistencePort) {
        this.bulkTransactionPersistencePort = bulkTransactionPersistencePort;
    }

    @Scheduled(cron = "*/5 * * * *")
    private void postTransactions() {
        bulkTransactionPersistencePort.postTransactions();
    }
    
    
    
    

}
