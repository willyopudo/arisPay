package org.arispay.ports.api;

import org.arispay.data.TransactionDto;

public interface TsqServicePort {
    TransactionDto queryTransaction(String bankRef, String bankCode);
}
