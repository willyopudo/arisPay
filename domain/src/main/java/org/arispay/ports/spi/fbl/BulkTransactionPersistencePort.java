package org.arispay.ports.spi.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkTransactionPersistencePort {
    BulkTransactionRequest addBulkTransaction(BulkTransactionRequest bulkTransactionRequest);

    void deleteBulkTransactionById(Long id);

    void updateBulkTransaction(BulkTransactionResponse bulkTransactionResponse, String processFlg);

    List<BulkTransactionResponse> getBulkTransactions();

    List<String> queryBulkTransactions(LocalDateTime nowTime, int timeInterval);
}
