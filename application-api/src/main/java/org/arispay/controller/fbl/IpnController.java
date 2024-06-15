package org.arispay.controller.fbl;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.controller.AuthController;
import org.arispay.data.GenericHttpResponse;
import org.arispay.data.fbl.dtorequest.ipn.FblIpnDto;
import org.arispay.entity.CompanyAccount;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fbl")
public class IpnController {
    @Autowired
    private CompanyAccountServicePort<CompanyAccount> companyAccountServicePort;
    private static final Logger logger = LogManager.getLogger(IpnController.class);

    @PostMapping("/ipn")
    public ResponseEntity<GenericHttpResponse> processIpnRequest(
            @Valid @RequestBody FblIpnDto ipnRequest) {
        //Request logging is handled by logging configuration bean
        if(ipnRequest != null && ipnRequest.getIPN().getTXN().getFirst().TXN_ACC != null) {
            CompanyAccount account = companyAccountServicePort.getByAccountNumber(ipnRequest.getIPN().getTXN().getFirst().TXN_ACC);
            if(account != null) {
                //Todo
                //Call service function to save to db if company with passed account exists
                logger.info("Account number was found {}", account.getAccountNumber());
            }
            else{
                //Todo
                //Save to rejected Transaction table
                logger.info("Account number was not found {}", ipnRequest.getIPN().getTXN().getFirst().TXN_ACC);
            }
        }

        GenericHttpResponse httpResponse = new GenericHttpResponse();
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setMessage("Transaction received successfully");

        return ResponseEntity.status(HttpStatus.OK)
                .body(httpResponse);
    }
}
