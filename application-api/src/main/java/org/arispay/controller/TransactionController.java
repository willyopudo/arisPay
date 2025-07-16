package org.arispay.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.*;
import org.arispay.ports.api.BankServicePort;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.api.TransactionServicePort;
import org.arispay.ports.api.TsqServicePort;
import org.javatuples.Pair;
import org.javatuples.Triplet;
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

    private final TransactionServicePort transactionServicePort;
    private final BankServicePort bankService;
    private final CompanyAccountServicePort<CompanyAccountDto> companyAccountService;
    private final TsqServicePort tsqService;

    private final JwtUtil jwtUtil;

    private static final Logger logger = LogManager.getLogger(TransactionController.class);

    public TransactionController(JwtUtil jwtUtil, TransactionServicePort transactionServicePort, BankServicePort bankService, CompanyAccountServicePort<CompanyAccountDto> companyAccountService, TsqServicePort tsqService) {
        this.jwtUtil = jwtUtil;
        this.transactionServicePort = transactionServicePort;
        this.bankService = bankService;
        this.companyAccountService = companyAccountService;
        this.tsqService = tsqService;
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
    // Retrieves all transactions with optional filters and pagination
    //Returns a paginated list of transactions along with a list of banks for selection and a list of company accounts for selection
    public ResponseEntity<Triplet<Page<TransactionDto>, List<List<SelectDto>>, ISummary>> getAllTransactions(@RequestParam(defaultValue = "0") int page,
                                                                                                             @RequestParam(defaultValue = "5") int itemsPerPage,
                                                                                                             @RequestParam(name = "bank", required = false, defaultValue = "") String bank,
                                                                                                             @RequestParam(name = "account", required = false, defaultValue = "") String account,
                                                                                                             @RequestParam(name = "crDrInd", required = false, defaultValue = "") String crDrInd,
                                                                                                             @RequestParam(name = "dateRange", required = false) List<LocalDate> dateRange,
                                                                                                             @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                                                                             @RequestParam(name = "sortBy", defaultValue = "transDate", required = false) String sortBy,
                                                                                                             @RequestParam(name = "orderBy", defaultValue = "asc", required = false) String orderBy,
                                                                                                             HttpServletRequest request,
                                                                                                             Authentication authentication) {
        logger.info("Authentication: {}", authentication.getAuthorities());

        Claims claims = jwtUtil.resolveClaims(request);

        // Determine sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(orderBy)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        logger.info("Date range submitted: {}", dateRange);
        GenericFilterDto filterDto = new GenericFilterDto(
                List.of( bank, dateRange == null ? "" : dateRange, account, crDrInd),
                search,
                direction,
                sortBy
        );

        Long companyId = claims.get("companyId", Long.class);

        List<List<SelectDto>> selectOptions = new ArrayList<>();
        List<SelectDto> banks = bankService.getBanks();
        List<SelectDto> companyAccounts = companyAccountService.getAccountsSelectList(companyId);
        selectOptions.add(banks);
        selectOptions.add(companyAccounts);

        ISummary transactionSummary = transactionServicePort.getTransactionSummaries(companyId).orElse(null);

        Pageable pageable = PageRequest.of(page-1, itemsPerPage);
        return ResponseEntity.ok(new Triplet<> (transactionServicePort.getTransactions(companyId, pageable, filterDto), selectOptions, transactionSummary));
    }

    @GetMapping("/query")
    public ResponseEntity<?> queryTransaction(@RequestParam (name = "transRef") String transRef,
                                                           @RequestParam (name = "bank") String bankCode,
                                                           HttpServletRequest request) {
        if (transRef == null || transRef.isEmpty() || bankCode == null || bankCode.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GenericFilterDto filterDto = new GenericFilterDto(
                List.of( bankCode, transRef, List.of() , "", ""),
                null,
                null,
                null
        );
        try{
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);
            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            TransactionDto transactionDto = transactionServicePort.queryTransactions(companyId,filterDto);
            if (transactionDto == null) {
                //Send TSQ request to the selected bank to try check if the transaction exists
                transactionDto = tsqService.queryTransaction(transRef, bankCode);
                if (transactionDto == null) {
                    return ResponseEntity.notFound().build();
                }
            }
            return ResponseEntity.ok(transactionDto);
        }
        catch (NoSuchElementException ex){
            logger.warn("Transaction not found: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        catch (Exception e) {
            logger.error("Error querying transaction: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTransactionByID(@PathVariable long id) {
        transactionServicePort.deleteTransactionById(id);
    }
}
