package org.arispay.adapters.fbl.httpclient;

import org.arispay.data.TransactionDto;
import org.arispay.ports.spi.httpclient.TsqHttpClientPort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("070")
public class TsqAdapter implements TsqHttpClientPort {


    @Override
    public TransactionDto queryTransaction(String bankRef) {
        //Make an HTTP call to the TSQ service using the bankRef
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setBankTranRef(bankRef);
        transactionDto.setNarration("Test transaction over TSQ"); // Assuming 070 is the bank code for TSQ
        return transactionDto;
    }
}
