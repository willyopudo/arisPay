package org.arispay.controller.fbl;

import jakarta.validation.Valid;
import org.arispay.data.GenericHttpResponse;
import org.arispay.data.fbl.dtorequest.confirmation.ConfirmationRequest;
import org.arispay.data.fbl.dtorequest.ipn.FblIpnDto;
import org.arispay.data.fbl.dtoresponse.confirmation.ConfirmationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fbl")
public class IpnController {
    @PostMapping("/ipn")
    public ResponseEntity<GenericHttpResponse> processIpnRequest(
            @Valid @RequestBody FblIpnDto ipnRequest) {
        //Request logging is handled by logging configuration bean
        //Todo
        //Call service function to check and extract contents of payload and save to db if all specifications are met
        GenericHttpResponse httpResponse = new GenericHttpResponse();
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setMessage("Transaction received successfully");

        return ResponseEntity.status(HttpStatus.OK)
                .body(httpResponse);
    }
}
