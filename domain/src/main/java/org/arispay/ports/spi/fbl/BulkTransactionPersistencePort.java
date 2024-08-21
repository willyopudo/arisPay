package org.arispay.ports.spi.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;

import java.util.List;

public interface BulkTransactionPersistencePort {
    BulkTransactionRequest addBulkTransaction(BulkTransactionRequest bulkTransactionRequest);

    void deleteBulkTransactionById(Long id);

    BulkTransactionResponse updateBulkTransaction(BulkTransactionResponse bulkTransactionResponse, String processFlg);

    List<BulkTransactionResponse> getBulkTransactions();
}
