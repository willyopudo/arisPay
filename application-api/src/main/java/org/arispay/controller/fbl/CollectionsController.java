package org.arispay.controller.fbl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.arispay.data.CompanyAccountDto;
import org.arispay.data.ClientDto;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.fbl.dtorequest.confirmation.ConfirmationRequest;
import org.arispay.data.fbl.dtorequest.validation.ValidationRequest;
import org.arispay.data.fbl.dtoresponse.confirmation.ConfirmationResponse;
import org.arispay.data.fbl.dtoresponse.validation.ValidationResponse;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.api.ClientServicePort;
import org.arispay.ports.api.TransactionServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/fbl")
public class CollectionsController {

    @Autowired
    private ClientServicePort clientServicePort;

    @Autowired
    private TransactionServicePort transactionServicePort;

    @Autowired
    private CompanyAccountServicePort<CompanyAccountDto> companyAccountServicePort;

    @PostMapping("/validation")
    public ResponseEntity<ValidationResponse> validateClient(@RequestBody ValidationRequest validationRequest) {

        ClientDto fetchedClient = clientServicePort.getClientById(validationRequest.getPayload().getIdentifier());
        ValidationResponse validationResponse = new ValidationResponse();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
    public ResponseEntity<ConfirmationResponse> validateClient(@RequestBody ConfirmationRequest confirmationRequest) {

        ConfirmationResponse confirmationResponse = new ConfirmationResponse();
        CompanyAccountDto fetchedAccount = companyAccountServicePort
                .getByAccountNumber(confirmationRequest.getPayload().getCollectionAccount());
        if (fetchedAccount == null) {
            confirmationResponse.setStatusCode("PAYMENT_ACK");
            confirmationResponse.setStatusDescription("Payment Transaction Received Successfully.");
        } else {
            // TransactionDto transaction =
            // transactionServicePort.getTransactionByTransactionId(confirmationRequest.getPayload().getTransaction
        }
        return null;
    }
}
