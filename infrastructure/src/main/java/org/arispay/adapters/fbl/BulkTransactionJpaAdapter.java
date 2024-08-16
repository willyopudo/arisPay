package org.arispay.adapters.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.entity.fbl.BulkTransaction;
import org.arispay.mappers.fbl.BulkTransactionMapper;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;
import org.arispay.repository.fbl.BulkTransactionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BulkTransactionJpaAdapter implements BulkTransactionPersistencePort {

    private final BulkTransactionRepository bulkTransactionRepository;

    private final BulkTransactionMapper bulkTransactionMapper;

    public BulkTransactionJpaAdapter(BulkTransactionRepository bulkTransactionRepository, BulkTransactionMapper bulkTransactionMapper) {
        this.bulkTransactionRepository = bulkTransactionRepository;
        this.bulkTransactionMapper = bulkTransactionMapper;
    }

    @Override
    public BulkTransactionRequest addBulkTransaction(BulkTransactionRequest bulkTransactionRequest) {
        BulkTransaction bulkTransaction = bulkTransactionMapper.bulkTransRequestToBulkTrans(bulkTransactionRequest);
        bulkTransaction.setProcessFlg("S");
        bulkTransaction = bulkTransactionRepository.save(bulkTransaction);
        return bulkTransactionMapper.bulkTransToBulkTransRequest(bulkTransaction);
    }

    @Override
    public void deleteBulkTransactionById(Long id) {
        bulkTransactionRepository.deleteById(id);
    }

    @Override
    public BulkTransactionResponse updateBulkTransaction(BulkTransactionResponse bulkTransactionResponse) {
        BulkTransaction bulkTransaction = bulkTransactionMapper.bulkTransResponseToBulkTrans(bulkTransactionResponse);
        bulkTransaction.setProcessTime(new Timestamp(System.currentTimeMillis()));
        bulkTransaction.setNoOfTries(bulkTransaction.getNoOfTries() + 1);
        bulkTransaction = bulkTransactionRepository.save(bulkTransaction);
        return bulkTransactionMapper.bulkTransToBulkTransResponse(bulkTransaction);
    }

    @Override
    public List<BulkTransactionResponse> getBulkTransactions() {
        return bulkTransactionMapper.bulkTransListToBulkTransResponseList(bulkTransactionRepository.findAll());
    }
}
