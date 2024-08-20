package org.arispay.adapters.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.entity.fbl.BulkTransaction;
import org.arispay.entity.fbl.Detail;
import org.arispay.mappers.fbl.BulkTransactionMapper;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;
import org.arispay.repository.fbl.BulkTransactionRepository;
import org.arispay.repository.fbl.DetailRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BulkTransactionJpaAdapter implements BulkTransactionPersistencePort {

    private final BulkTransactionRepository bulkTransactionRepository;

    private final BulkTransactionMapper bulkTransactionMapper;

    private final DetailRepository detailRepository;

    public BulkTransactionJpaAdapter(BulkTransactionRepository bulkTransactionRepository,
                                     BulkTransactionMapper bulkTransactionMapper,
                                     DetailRepository detailRepository) {
        this.bulkTransactionRepository = bulkTransactionRepository;
        this.bulkTransactionMapper = bulkTransactionMapper;
        this.detailRepository = detailRepository;
    }

    @Override
    public BulkTransactionRequest addBulkTransaction(BulkTransactionRequest bulkTransactionRequest) {
        BulkTransaction bulkTransaction = bulkTransactionMapper.bulkTransRequestToBulkTrans(bulkTransactionRequest);
        bulkTransaction.setProcessFlg("S");
        bulkTransaction = bulkTransactionRepository.save(bulkTransaction);
        bulkTransaction.setBatchRef("BULK70" + String.format("%0" + 12 + "d", bulkTransaction.getId()));

        for (Detail detail: bulkTransaction.getDtl()) {
            detail.setBatchRef("BULK70" + String.format("%0" + 12 + "d", bulkTransaction.getId()));
            detail.setPaymentRef("DET70" + String.format("%0" + 12 + "d", detail.getId()));
        }

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
        bulkTransaction.setId(Long.parseLong(bulkTransaction.getBatchRef().replace("BULK70", "")));
        bulkTransactionRepository.updateStatus(bulkTransaction.getId(),
                bulkTransaction.getStatus(),
                bulkTransaction.getStatusDescription(),
                bulkTransaction.getCbsRef(),
                "A",
                new Timestamp(System.currentTimeMillis()));
        for (Detail detail: bulkTransaction.getDtl()) {
            detail.setId(Long.parseLong(detail.getPaymentRef().replace("DET70", "")));
            detailRepository.updateStatus(detail.getId(), detail.getStatus(), detail.getStatusDescription(), detail.getExternalRef(), detail.getCbsRef(), detail.getXRate());
        }

        return bulkTransactionMapper.bulkTransToBulkTransResponse(bulkTransaction);
    }

    @Override
    public List<BulkTransactionResponse> getBulkTransactions() {
        return bulkTransactionMapper.bulkTransListToBulkTransResponseList(bulkTransactionRepository.findAll());
    }
}
