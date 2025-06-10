package org.arispay.controller;

import java.util.List;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.GenericFilterDto;
import org.arispay.data.TransactionDto;
import org.arispay.ports.api.TransactionServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private TransactionServicePort transactionServicePort;

    private final JwtUtil jwtUtil;

    private static final Logger logger = LogManager.getLogger(TransactionController.class);

    public TransactionController(JwtUtil jwtUtil, TransactionServicePort transactionServicePort) {
        this.jwtUtil = jwtUtil;
        this.transactionServicePort = transactionServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto addTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionServicePort.addTransaction(transactionDto);
    }

    @PutMapping
    public TransactionDto updateTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionServicePort.updateTransaction(transactionDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionByID(@PathVariable long id) {
        return ResponseEntity.ok(transactionServicePort.getTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionDto>> getAllTransactions(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int itemsPerPage,
                                                                   @RequestParam(name = "clientId", required = false, defaultValue = "") String clientId,
                                                                   @RequestParam(name = "transDateRange", required = false, defaultValue = "") String transDateRange,
                                                                   @RequestParam(name = "sortBy", defaultValue = "transDate", required = false) String sortBy,
                                                                   @RequestParam(name = "orderBy", defaultValue = "asc", required = false) String orderBy,
                                                                   HttpServletRequest request,
                                                                   Authentication authentication) {
        logger.info("Authentication: {}", authentication.getAuthorities());

        Claims claims = jwtUtil.resolveClaims(request);

        // Determine sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(orderBy)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        GenericFilterDto filterDto = new GenericFilterDto(
                List.of( clientId, transDateRange),
                null,
                direction,
                sortBy
        );

        Long companyId = claims.get("companyId", Long.class);

        Pageable pageable = PageRequest.of(page-1, itemsPerPage);
        return ResponseEntity.ok(transactionServicePort.getTransactions(companyId, pageable, filterDto));
    }

    @DeleteMapping("/{id}")
    public void deleteTransactionByID(@PathVariable long id) {
        transactionServicePort.deleteTransactionById(id);
    }
}
