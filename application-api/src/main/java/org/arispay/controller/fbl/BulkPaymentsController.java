package org.arispay.controller.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.service.fbl.BulkPaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fbl/bulk-payments")
public class BulkPaymentsController {
    private final BulkPaymentService bulkPaymentService;

    public BulkPaymentsController(BulkPaymentService bulkPaymentService) {
        this.bulkPaymentService = bulkPaymentService;
    }

    @PostMapping("/initiate")
    public BulkTransactionResponse initiateBulkTransactionRequest(@RequestBody BulkTransactionRequest bulkTransactionRequest) {
        return bulkPaymentService.initiateBulkTransactionRequest(bulkTransactionRequest);
    }
}
