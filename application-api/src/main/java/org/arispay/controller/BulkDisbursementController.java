package org.arispay.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.BulkDisbursementParseResult;
import org.arispay.data.BulkDisbursementUploadDto;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.ports.api.BulkDisbursementFileParserPort;
import org.arispay.ports.api.NotificationServicePort;
import org.arispay.service.BulkDisbursementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bulk-disbursements")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class BulkDisbursementController {

    private static final Logger logger = LogManager.getLogger(BulkDisbursementController.class);

    private final BulkDisbursementService bulkDisbursementService;
    private final BulkDisbursementFileParserPort fileParserPort;
    private final NotificationServicePort notificationService;
    private final JwtUtil jwtUtil;

    public BulkDisbursementController(BulkDisbursementService bulkDisbursementService,
                                      BulkDisbursementFileParserPort fileParserPort,
                                      NotificationServicePort notificationService,
                                      JwtUtil jwtUtil) {
        this.bulkDisbursementService = bulkDisbursementService;
        this.fileParserPort = fileParserPort;
        this.notificationService = notificationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BulkDisbursementParseResult> parseFile(@RequestParam("file") MultipartFile file) {
        logger.info("Parsing bulk disbursement file: {}", file.getOriginalFilename());
        BulkDisbursementParseResult result = fileParserPort.parseFile(file);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitBulkDisbursement(@RequestBody BulkDisbursementUploadDto uploadDto,
                                                    HttpServletRequest request) {
        logger.info("Submitting bulk disbursement for bank: {}, items: {}",
                uploadDto.getBankCode(),
                uploadDto.getItems() != null ? uploadDto.getItems().size() : 0);

        try {
            BulkTransactionResponse response = bulkDisbursementService.submitBulkDisbursement(uploadDto);

            // Send notification
            try {
                Claims claims = jwtUtil.resolveClaims(request);
                Long companyId = claims.get("companyId", Long.class);
                int count = uploadDto.getItems() != null ? uploadDto.getItems().size() : 0;
                double totalAmount = uploadDto.getItems() != null
                        ? uploadDto.getItems().stream().mapToDouble(i -> i.getAmount()).sum()
                        : 0;
                notificationService.createBulkPaymentNotification(count, totalAmount, companyId, "INITIATED");
            } catch (Exception e) {
                logger.error("Error sending bulk disbursement notification: {}", e.getMessage(), e);
            }

            return ResponseEntity.ok(response);
        } catch (UnsupportedOperationException e) {
            logger.warn("Unsupported bank for disbursement: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing bulk disbursement: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing bulk disbursement: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BulkTransactionResponse>> getBulkDisbursements() {
        List<BulkTransactionResponse> disbursements = bulkDisbursementService.getBulkDisbursements();
        return ResponseEntity.ok(disbursements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BulkTransactionResponse> getBulkDisbursementById(@PathVariable Long id) {
        List<BulkTransactionResponse> all = bulkDisbursementService.getBulkDisbursements();
        return all.stream()
                .filter(d -> d.getBatchRef() != null && d.getBatchRef().contains(String.format("%012d", id)))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
