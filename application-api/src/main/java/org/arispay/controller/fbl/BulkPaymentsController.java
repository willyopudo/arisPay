package org.arispay.controller.fbl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.ports.api.NotificationServicePort;
import org.arispay.service.fbl.BulkPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fbl/bulk-payments")
public class BulkPaymentsController {
    private static final Logger logger = LogManager.getLogger(BulkPaymentsController.class);

    private final BulkPaymentService bulkPaymentService;

    @Autowired
    private NotificationServicePort notificationService;

    @Autowired
    private JwtUtil jwtUtil;

    public BulkPaymentsController(BulkPaymentService bulkPaymentService) {
        this.bulkPaymentService = bulkPaymentService;
    }

    @PostMapping("/initiate")
    public BulkTransactionResponse initiateBulkTransactionRequest(
            @RequestBody BulkTransactionRequest bulkTransactionRequest,
            HttpServletRequest request) {
        BulkTransactionResponse response = bulkPaymentService.initiateBulkTransactionRequest(bulkTransactionRequest);

        try {
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);
            int count = bulkTransactionRequest.getDtl() != null ? bulkTransactionRequest.getDtl().size() : 0;
            notificationService.createBulkPaymentNotification(
                    count, bulkTransactionRequest.getTotalAmount(), companyId, "INITIATED");
        } catch (Exception e) {
            logger.error("Error sending bulk payment notification: {}", e.getMessage(), e);
        }

        return response;
    }
}
