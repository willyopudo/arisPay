package org.arispay.ports.spi.httpclient;

import org.arispay.data.TransactionDto;

public interface TsqHttpClientPort {
    TransactionDto queryTransaction(String bankRef);
}
