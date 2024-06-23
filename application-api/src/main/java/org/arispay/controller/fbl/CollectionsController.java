package org.arispay.controller.fbl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.GenericHttpResponse;
import org.arispay.data.TransactionDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.ClientDto;
import org.arispay.data.fbl.dtorequest.confirmation.ConfirmationRequest;
import org.arispay.data.fbl.dtorequest.validation.ValidationRequest;
import org.arispay.data.fbl.dtoresponse.confirmation.ConfirmationResponse;
import org.arispay.data.fbl.dtoresponse.validation.ValidationResponse;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.api.TransactionRejectedServicePort;
import org.arispay.ports.api.ClientServicePort;
import org.arispay.ports.api.TransactionServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/fbl/collections")
public class CollectionsController {

    @Autowired
    private ClientServicePort clientServicePort;

    @Autowired
    private TransactionServicePort transactionServicePort;

    @Autowired
    private TransactionRejectedServicePort transactionRejectedServicePort;

    @Autowired
    private CompanyAccountServicePort<CompanyAccountDto> companyAccountServicePort;

    private static final Logger logger = LogManager.getLogger(CollectionsController.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/validation")
    public ResponseEntity<ValidationResponse> validateClient(@RequestBody ValidationRequest validationRequest) {

        ClientDto fetchedClient = clientServicePort.getClientById(validationRequest.getPayload().getIdentifier());
        ValidationResponse validationResponse = new ValidationResponse();
        if (fetchedClient == null) {
            validationResponse.setStatus_code("ACCOUNT_NOT_FOUND");
            validationResponse.setStatus_description("ACCOUNT IS NOT VALID");
            validationResponse.setDate_time(LocalDateTime.now().format(formatter));

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(validationResponse);
        } else {
            validationResponse.setStatus_code("ACCOUNT_FOUND");
            validationResponse.setStatus_description("ACCOUNT IS VALID");
            validationResponse.setDate_time(LocalDateTime.now().format(formatter));
            validationResponse.getPayload().setIdentifier(fetchedClient.getClientId());
            validationResponse.getPayload().setCustomer_id(fetchedClient.getClientId());
            validationResponse.getPayload().setIdentifier_type(validationRequest.getPayload().getIdentifier_type());
            validationResponse.getPayload().setCustomer_name(fetchedClient.getClientName());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(validationResponse);

        }

    }

    @PostMapping("/confirmation")
    public ResponseEntity validateClient(@RequestBody ConfirmationRequest confirmationRequest) {
        ConfirmationResponse confirmationResponse = new ConfirmationResponse();
        try {
            LocalDateTime dateTime = LocalDateTime.parse(confirmationRequest.getPayload().getDateTime(), formatter);

            String collectionAccount = confirmationRequest.getPayload().getCollectionAccount();
            String customerId = confirmationRequest.getPayload().getCustomerId();

            CompanyAccountDto fetchedAccount = companyAccountServicePort.getByAccountNumber(collectionAccount);
            if (fetchedAccount == null) {
                confirmationResponse.setStatusDescription(
                        "Payment Transaction Received Successfully. Note: collection_account is not correct");
            }

            ClientDto fetchedClient = null;
            if (fetchedAccount != null) {
                fetchedClient = clientServicePort.getClientByIdAndCompany(fetchedAccount.getCompanyId(), customerId);
                if (fetchedClient == null) {
                    confirmationResponse.setStatusDescription(
                            "Payment Transaction Received Successfully. Note: customer_id is not correct");
                }
            }

            confirmationResponse.setStatusCode("PAYMENT_ACK");
            confirmationResponse.setStatusDescription(confirmationResponse.getStatusDescription() == null
                    ? "Payment Transaction Received Successfully."
                    : confirmationResponse.getStatusDescription());

            TransactionDto transaction = new TransactionDto(
                    confirmationRequest.getPayload().getTxnReference(), null,
                    confirmationRequest.getPayload().getTxnAmount(),
                    collectionAccount, fetchedAccount != null ? fetchedAccount.getCompanyId() : null,
                    customerId, confirmationRequest.getPayload().getPayerName(),
                    confirmationRequest.getPayload().getPayerPhone(), confirmationRequest.getPayload().getPaymentMode(),
                    confirmationRequest.getPayload().getTxnNarration(), "/api/v1/fbl/confirmation", dateTime, "C");

            if (fetchedAccount == null || fetchedClient == null) {
                transaction = transactionRejectedServicePort.addTransaction(transaction);
            } else {
                transaction = transactionServicePort.addTransaction(transaction);
            }

            confirmationResponse.setPaymentRef(transaction.getArisTranRef());
            confirmationResponse.setDateTime(LocalDateTime.now().format(formatter));
        } catch (DataIntegrityViolationException ex) {
            logger.error(ex.getMessage(), ex);
            GenericHttpResponse httpResponse = new GenericHttpResponse();
            httpResponse.setHttpStatus(HttpStatus.CONFLICT);
            if (ex.getMessage().contains("Duplicate")) {
                httpResponse.setMessage("Duplicate request for transaction reference: "
                        + confirmationRequest.getPayload().getTxnReference());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(httpResponse);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            GenericHttpResponse httpResponse = new GenericHttpResponse();
            httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            httpResponse.setMessage("An error occurred while processing IPN request");
            return ResponseEntity.internalServerError().body(httpResponse);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(confirmationResponse);
    }
}
