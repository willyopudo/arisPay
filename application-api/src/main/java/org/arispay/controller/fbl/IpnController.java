package org.arispay.controller.fbl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.GenericHttpResponse;
import org.arispay.data.TransactionDto;
import org.arispay.data.fbl.dtorequest.ipn.FblIpnDto;
import org.arispay.data.fbl.dtorequest.ipn.TXN;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.api.TransactionRejectedServicePort;
import org.arispay.ports.api.TransactionServicePort;
import org.arispay.utils.GlobalHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "FBL IPN", description = "IPN API for Family Bank")
@RequestMapping("/api/v1/fbl")
public class IpnController {
    @Autowired
    private CompanyAccountServicePort<CompanyAccountDto> companyAccountServicePort;
    @Autowired
    private TransactionServicePort transactionServicePort;
    @Autowired
    private TransactionRejectedServicePort transactionRejectedServicePort;
    private static final Logger logger = LogManager.getLogger(IpnController.class);
    @Autowired
    GlobalHelpers globalHelpers;

    @PostMapping("/ipn")
    public ResponseEntity<GenericHttpResponse<?>> processIpnRequest(
            @Valid @RequestBody FblIpnDto ipnRequest) throws ParseException {
        GenericHttpResponse<?> httpResponse = new GenericHttpResponse<String>();
        // Request logging is handled by logging configuration bean
        if (globalHelpers.isNotBlank(ipnRequest)) {
            try {
                for (TXN txn : ipnRequest.getIPN().getTXN()) {
                    if (globalHelpers.isNotBlank(txn.TXN_ACC)) {
                        CompanyAccountDto account = companyAccountServicePort
                                .getByAccountNumber(ipnRequest.getIPN().getTXN().getFirst().TXN_ACC);

                        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        TransactionDto transactionDto = new TransactionDto(0L,txn.getTXN_REF(), null,
                                Double.valueOf(txn.getTXN_DETAIL().getFirst().getTXN_AMT()), txn.getTXN_ACC(),
                                "070",
                                0L,
                                null,
                                null,
                                null, txn.getTXN_DETAIL().getFirst().TXN_CODE,
                                txn.getTXN_DETAIL().getFirst().TXN_NARRATION, "IPN",
                                LocalDateTime.parse((ipnRequest.getIPN().getDATE()), formatter),
                                txn.getTXN_DETAIL().getFirst().TXN_TYPE);

                        if (account != null) {
                            transactionDto.setCompanyId(account.getCompanyId());
                            logger.info("Account number was found {}", account.getAccountNumber());
                            // Todo

                            // Call service function to save to db if company with passed account exists

                            TransactionDto savedTrans = transactionServicePort.addTransaction(transactionDto);
                            logger.info("Transaction record added success : {}", savedTrans.toString());

                        } else {
                            logger.info("Account number was not found {}",
                                    ipnRequest.getIPN().getTXN().getFirst().TXN_ACC);
                            // Todo
                            // Save to rejected Transaction table
                            TransactionDto savedTrans = transactionRejectedServicePort.addTransaction(transactionDto);
                            logger.info("Transaction record added to rejected : {}", savedTrans.toString());

                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                httpResponse.setMessage("An error occurred while processing IPN request");
                if(e.getMessage().contains("Duplicate")){
                    httpResponse.setHttpStatus(HttpStatus.CONFLICT);
                    httpResponse.setMessage("Transaction record with TransRef " + ipnRequest.getIPN().getTXN().getFirst().TXN_REF + " already exists");
                    return new ResponseEntity<>(httpResponse, HttpStatus.CONFLICT);
                }

                return ResponseEntity.internalServerError().body(httpResponse);

            }

        }
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setMessage("Transaction received successfully");

        return ResponseEntity.status(HttpStatus.OK)
                .body(httpResponse);
    }
}
