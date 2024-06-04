package org.arispay.controller;

import org.arispay.data.fbl.dtorequest.confirmation.ConfirmationRequest;
import org.arispay.data.dtoresponse.confirmation.ConfirmationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/confirmation")
public class ConfirmationController {

    @PostMapping("/familybank")
    public ResponseEntity<ConfirmationResponse> postPaymentNotification(@RequestBody ConfirmationRequest confirmationRequest) {

        ConfirmationResponse confirmationResponse = new ConfirmationResponse();

        return ResponseEntity.status(HttpStatus.OK)
                .body(confirmationResponse);
    }
}
