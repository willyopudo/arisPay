package org.arispay.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.TransactionDto;
import org.arispay.ports.api.TsqServicePort;
import org.arispay.ports.spi.httpclient.TsqHttpClientPort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TsqServiceImpl implements TsqServicePort {
    private final Map<String, TsqHttpClientPort> tsqHttpClientsMap;
    private static final Logger logger = LogManager.getLogger(TsqServiceImpl.class);

    public TsqServiceImpl(Map<String, TsqHttpClientPort> tsqHttpClientsMap) {
        this.tsqHttpClientsMap = tsqHttpClientsMap;
    }
    @Override
    public TransactionDto queryTransaction(String bankRef, String bankCode) {

        // Sending a request to the TSQ service to query a transaction by bank reference
        TsqHttpClientPort tsqHttpClientPort = tsqHttpClientsMap.get(bankCode);
        if (tsqHttpClientPort == null) {
            logger.warn("Tsq HTTP Client not found for Bank Code {}", bankCode);
            throw new NoSuchElementException("No transaction found for bank reference: " + bankRef);
        }
        TransactionDto transactionDto = tsqHttpClientPort.queryTransaction(bankRef);
        if (transactionDto == null) {
            throw new NoSuchElementException("No transaction found for bank reference: " + bankRef);
        }
        // Assuming the transactionDto is populated with the necessary data
        return transactionDto;
    }
}
