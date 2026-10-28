package org.arispay.service;

import org.arispay.data.BulkDisbursementUploadDto;
import org.arispay.data.DisbursementItemDto;
import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtorequest.masspayments.DetailRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.ports.api.BankDisbursementServicePort;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BulkDisbursementService {

    private final Map<String, BankDisbursementServicePort> bankServices;
    private final BulkTransactionPersistencePort bulkTransactionPersistencePort;

    public BulkDisbursementService(List<BankDisbursementServicePort> services,
                                   BulkTransactionPersistencePort bulkTransactionPersistencePort) {
        this.bankServices = services.stream()
                .collect(Collectors.toMap(
                        BankDisbursementServicePort::getBankCode,
                        s -> s
                ));
        this.bulkTransactionPersistencePort = bulkTransactionPersistencePort;
    }

    public BankDisbursementServicePort resolve(String bankCode) {
        BankDisbursementServicePort service = bankServices.get(bankCode);
        if (service == null) {
            throw new UnsupportedOperationException("No disbursement service available for bank: " + bankCode);
        }
        return service;
    }

    public BulkTransactionResponse submitBulkDisbursement(BulkDisbursementUploadDto uploadDto) {
        String bankCode = uploadDto.getBankCode();
        BankDisbursementServicePort bankService = resolve(bankCode);

        BulkTransactionRequest request = buildRequest(uploadDto);

        // Persist the bulk transaction
        request = bulkTransactionPersistencePort.addBulkTransaction(request);

        // Process via the resolved bank service
        BulkTransactionResponse response = bankService.processBulkDisbursement(request);

        // Update with response
        if (response != null) {
            bulkTransactionPersistencePort.updateBulkTransaction(response, "A");
        }

        return response;
    }

    private BulkTransactionRequest buildRequest(BulkDisbursementUploadDto uploadDto) {
        List<DetailRequest> details = uploadDto.getItems().stream()
                .map(this::toDetailRequest)
                .collect(Collectors.toList());

        double totalAmount = uploadDto.getItems().stream()
                .mapToDouble(DisbursementItemDto::getAmount)
                .sum();

        BulkTransactionRequest request = new BulkTransactionRequest();
        request.setAccountDr(uploadDto.getAccountDr());
        request.setNarration(uploadDto.getNarration());
        request.setCurrency(uploadDto.getCurrency() != null ? uploadDto.getCurrency() : "KES");
        request.setValueDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        request.setTotalAmount(totalAmount);
        request.setDtl(details);

        return request;
    }

    private DetailRequest toDetailRequest(DisbursementItemDto item) {
        DetailRequest detail = new DetailRequest();
        detail.setBeneficiaryAccount(item.getBeneficiaryAccount());
        detail.setBeneficiaryBank(item.getBeneficiaryBank());
        detail.setBeneficiaryDetails(item.getBeneficiaryName());
        detail.setPaymentType(item.getPaymentType() != null ? item.getPaymentType() : "EFT");
        detail.setCurrency(item.getCurrency() != null ? item.getCurrency() : "KES");
        detail.setPaymentAmount(item.getAmount());
        detail.setPurpose(item.getPurpose());
        detail.setRemarks(item.getRemarks());
        return detail;
    }

    public List<BulkTransactionResponse> getBulkDisbursements() {
        return bulkTransactionPersistencePort.getBulkTransactions();
    }
}
