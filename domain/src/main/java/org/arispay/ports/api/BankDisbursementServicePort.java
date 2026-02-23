package org.arispay.ports.api;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;

public interface BankDisbursementServicePort {
    String getBankCode();
    BulkTransactionResponse processBulkDisbursement(BulkTransactionRequest request);
}
