package org.arispay.ports.api.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkTransactionServicePort {
    BulkTransactionRequest addBulkTransaction(BulkTransactionRequest bulkTransactionRequest);

    void deleteBulkTransactionById(Long id);

    BulkTransactionResponse updateBulkTransaction(BulkTransactionResponse bulkTransactionResponse);

    List<BulkTransactionResponse> getBulkTransactions();

    List<String> queryBulkTransactions(LocalDateTime nowTime, int timeInterval);
}
