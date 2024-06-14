package org.arispay.controller.fbl;

import jakarta.validation.Valid;
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

    @PostMapping("/ipn")
    public ResponseEntity<GenericHttpResponse> processIpnRequest(
            @Valid @RequestBody FblIpnDto ipnRequest) {
        //Request logging is handled by logging configuration bean
        if(ipnRequest != null && ipnRequest.getIPN().getTXN().getFirst().TXN_ACC != null) {
            CompanyAccount account = companyAccountServicePort.getByAccountNumber(ipnRequest.getIPN().getTXN().getFirst().TXN_ACC);
            if(account != null) {
                //Todo
                //Call service function to save to db if company with passed account exists
            }
            else{
                //Todo
                //Save to rejected Transaction table
            }
        }

        GenericHttpResponse httpResponse = new GenericHttpResponse();
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setMessage("Transaction received successfully");

        return ResponseEntity.status(HttpStatus.OK)
                .body(httpResponse);
    }
}
