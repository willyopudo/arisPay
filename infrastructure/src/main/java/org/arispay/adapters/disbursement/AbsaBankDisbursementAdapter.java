package org.arispay.adapters.disbursement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.ports.api.BankDisbursementServicePort;
import org.springframework.stereotype.Service;

@Service
public class AbsaBankDisbursementAdapter implements BankDisbursementServicePort {

    private static final Logger logger = LogManager.getLogger(AbsaBankDisbursementAdapter.class);
    private static final String BANK_CODE = "003";

    @Override
    public String getBankCode() {
        return BANK_CODE;
    }

    @Override
    public BulkTransactionResponse processBulkDisbursement(BulkTransactionRequest request) {
        logger.warn("ABSA Bank disbursement service is not yet implemented");
        throw new UnsupportedOperationException("ABSA Bank disbursement integration is not yet available");
    }
}
