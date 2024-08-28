package org.arispay.adapters.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.entity.Transaction;
import org.arispay.entity.fbl.BulkTransaction;
import org.arispay.entity.fbl.Detail;
import org.arispay.mappers.fbl.BulkTransactionMapper;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;
import org.arispay.repository.TransactionRepository;
import org.arispay.repository.fbl.BulkTransactionRepository;
import org.arispay.repository.fbl.DetailRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BulkTransactionJpaAdapter implements BulkTransactionPersistencePort {

    private final BulkTransactionRepository bulkTransactionRepository;

    private final BulkTransactionMapper bulkTransactionMapper;

    private final DetailRepository detailRepository;

    private final TransactionRepository transactionRepository;

    public BulkTransactionJpaAdapter(BulkTransactionRepository bulkTransactionRepository,
                                     BulkTransactionMapper bulkTransactionMapper,
                                     DetailRepository detailRepository,
                                     TransactionRepository transactionRepository) {
        this.bulkTransactionRepository = bulkTransactionRepository;
        this.bulkTransactionMapper = bulkTransactionMapper;
        this.detailRepository = detailRepository;
        this.transactionRepository = transactionRepository;
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
    public void updateBulkTransaction(BulkTransactionResponse bulkTransactionResponse, String processFlg) {
        BulkTransaction bulkTransaction = bulkTransactionMapper.bulkTransResponseToBulkTrans(bulkTransactionResponse);
        try {

            bulkTransaction.setId(Long.parseLong(bulkTransaction.getBatchRef().replace("BULK70", "")));
            bulkTransaction.setProcessFlg(processFlg);
            bulkTransaction.setProcessTime(LocalDateTime.now());
            bulkTransactionRepository.updateStatus(bulkTransaction.getId(),
                    bulkTransaction.getStatus(),
                    bulkTransaction.getStatusDescription(),
                    bulkTransaction.getCbsRef(),
                    bulkTransaction.getProcessFlg(),
                    bulkTransaction.getProcessTime(),
                    bulkTransaction.getNoOfTries() + 1);
            for (Detail detail : bulkTransaction.getDtl()) {
                detail.setId(Long.parseLong(detail.getPaymentRef().replace("DET70", "")));
            }

            detailRepository.batchDetailUpdate(bulkTransaction.getDtl());

            bulkTransactionMapper.bulkTransToBulkTransResponse(bulkTransaction);
        }
        catch (Exception e) {
            e.printStackTrace();
            bulkTransactionRepository.updateStatus(bulkTransaction.getId(),
                    null,
                    null,
                    null,
                    "X",
                    bulkTransaction.getProcessTime(),
                    bulkTransaction.getNoOfTries() + 1);
        }
    }

    @Override
    public List<BulkTransactionResponse> getBulkTransactions() {
        return bulkTransactionMapper.bulkTransListToBulkTransResponseList(bulkTransactionRepository.findAll());
    }

    @Override
    public List<String> queryBulkTransactions(LocalDateTime nowTime, int timeInterval) {
        return bulkTransactionRepository.findPendingTransactions(nowTime, timeInterval);
    }

    @Override
    public void markProcessingStage(Long id, String processFlg) {
        bulkTransactionRepository.markProcessingStage(id, processFlg);
    }

    @Override
    public void postTransactions() {
        try {
            List<BulkTransaction> unpostedTransactions = bulkTransactionRepository.findUnpostedTransactions();
            for (BulkTransaction bulkTransaction : unpostedTransactions) {
                try {
                    bulkTransactionRepository.markPostingStage(bulkTransaction.getId(), "S");
                    for (Detail detail : bulkTransaction.getDtl()) {
                        Transaction transaction = new Transaction();
                        transaction.setBankAccount(bulkTransaction.getAccountDr());
                        transaction.setBankTranRef(detail.getCbsRef());
                        transaction.setNarration(detail.getRemarks());
                        transaction.setPayerName(detail.getSenderDetails());
                        transaction.setPayerPhone(detail.getSenderDetails());
                        transaction.setPaymentMode(detail.getPaymentType());
                        transaction.setTranAmount(detail.getPaymentAmount());
                        transaction.setTransDate(bulkTransaction.getValueDate());
                        transaction.setCrDrInd("C");
                        transaction.setCreatedBy("SYSTEM");
                        transaction.setCreatedDate(LocalDateTime.now());
                        transaction.setApiChannel("/api/v1/fbl/bulk-payments/initiate");

                    }
                    bulkTransactionRepository.markPostingStageFinal(bulkTransaction.getId(), "P", LocalDateTime.now(), bulkTransaction.getPostingTryCount() + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    bulkTransactionRepository.markPostingStageFinal(bulkTransaction.getId(), "X", LocalDateTime.now(), bulkTransaction.getPostingTryCount() + 1);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
