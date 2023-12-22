package org.arispay.controller;

import org.arispay.data.dtorequest.Validation.ValidationRequest;
import org.arispay.data.dtoresponse.Validation.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validation")
public class ValidationController {

    @PostMapping("/familybank")
    public ResponseEntity<ValidationResponse> validateClient(@RequestBody ValidationRequest validationRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        return ResponseEntity.status(HttpStatus.OK)
                .body(validationResponse);
    }

}
