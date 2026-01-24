package org.arispay.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.*;
import org.arispay.ports.api.ActivityServicePort;
import org.arispay.ports.api.DashboardServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DashboardController {

    private static final Logger logger = LogManager.getLogger(DashboardController.class);

    private final DashboardServicePort dashboardServicePort;
    private final ActivityServicePort activityServicePort;
    private final JwtUtil jwtUtil;


    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary(HttpServletRequest request) {
        try {
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);

            if (companyId == null) {
                logger.error("CompanyId not found in JWT claims");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Fetching dashboard summary for company: {}", companyId);
            DashboardSummaryDto summary = dashboardServicePort.getDashboardSummary(companyId);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error fetching dashboard summary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/widgets")
    public ResponseEntity<DashboardWidgetsDto> getWidgets(HttpServletRequest request) {
        try {
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);

            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Fetching widgets for company: {}", companyId);
            DashboardWidgetsDto widgets = dashboardServicePort.getWidgets(companyId);
            return ResponseEntity.ok(widgets);
        } catch (Exception e) {
            logger.error("Error fetching widgets: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/earning-reports")
    public ResponseEntity<List<EarningReportDto>> getEarningReports(
            @RequestParam(defaultValue = "12") int months,
            HttpServletRequest request) {
        try {
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);

            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Fetching earning reports for company: {} (last {} months)", companyId, months);
            List<EarningReportDto> reports = dashboardServicePort.getEarningReports(companyId, months);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            logger.error("Error fetching earning reports: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/latest-transactions")
    public ResponseEntity<List<LatestTransactionDto>> getLatestTransactions(
            @RequestParam(defaultValue = "5") int limit,
            HttpServletRequest request) {
        try {
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);

            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Fetching latest {} transactions for company: {}", limit, companyId);
            List<LatestTransactionDto> transactions = dashboardServicePort.getLatestTransactions(companyId, limit);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error fetching latest transactions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top-clients")
    public ResponseEntity<List<TopClientDto>> getTopClients(
            @RequestParam(defaultValue = "6") int limit,
            HttpServletRequest request) {
        try {
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);

            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Fetching top {} clients for company: {}", limit, companyId);
            List<TopClientDto> topClients = dashboardServicePort.getTopClients(companyId, limit);
            return ResponseEntity.ok(topClients);
        } catch (Exception e) {
            logger.error("Error fetching top clients: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recent-activities")
    public ResponseEntity<List<ActivityEventDto>> getRecentActivities(
            @RequestParam(defaultValue = "20") int limit,
            HttpServletRequest request) {
        try {
            Claims claims = jwtUtil.resolveClaims(request);
            Long companyId = claims.get("companyId", Long.class);

            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            logger.info("Fetching recent {} activities for company: {}", limit, companyId);
            List<ActivityEventDto> activities = activityServicePort.getRecentActivities(companyId, limit);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            logger.error("Error fetching recent activities: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
