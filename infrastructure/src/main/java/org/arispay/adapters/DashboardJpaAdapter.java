package org.arispay.adapters;

import org.arispay.data.*;
import org.arispay.ports.spi.DashboardPersistencePort;
import org.arispay.repository.DashboardRepository;
import org.arispay.repository.projections.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardJpaAdapter implements DashboardPersistencePort {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Override
    public DashboardWidgetsDto getWidgets(Long companyId) {
        TransactionMetricsProjection metrics = dashboardRepository.getTransactionMetrics(companyId);

        if (metrics == null) {
            // Return empty widgets if no data
            return DashboardWidgetsDto.builder()
                    .collections(buildEmptyMetric())
                    .disbursements(buildEmptyMetric())
                    .totalCollections(buildEmptyMetric())
                    .totalDisbursements(buildEmptyMetric())
                    .build();
        }

        return DashboardWidgetsDto.builder()
                .collections(buildMetric(
                        metrics.getCurrentCollectionCount() != null ? metrics.getCurrentCollectionCount().doubleValue() : 0.0,
                        metrics.getPreviousCollectionCount() != null ? metrics.getPreviousCollectionCount().doubleValue() : 0.0
                ))
                .disbursements(buildMetric(
                        metrics.getCurrentDisbursementCount() != null ? metrics.getCurrentDisbursementCount().doubleValue() : 0.0,
                        metrics.getPreviousDisbursementCount() != null ? metrics.getPreviousDisbursementCount().doubleValue() : 0.0
                ))
                .totalCollections(buildMetric(
                        metrics.getCurrentCollectionAmount() != null ? metrics.getCurrentCollectionAmount() : 0.0,
                        metrics.getPreviousCollectionAmount() != null ? metrics.getPreviousCollectionAmount() : 0.0
                ))
                .totalDisbursements(buildMetric(
                        metrics.getCurrentDisbursementAmount() != null ? metrics.getCurrentDisbursementAmount() : 0.0,
                        metrics.getPreviousDisbursementAmount() != null ? metrics.getPreviousDisbursementAmount() : 0.0
                ))
                .build();
    }

    @Override
    public List<EarningReportDto> getEarningReports(Long companyId, int months) {
        List<EarningReportProjection> projections = dashboardRepository.getEarningReports(companyId, months);

        if (projections == null || projections.isEmpty()) {
            return Collections.emptyList();
        }

        // Group by month and aggregate payment modes
        Map<String, Map<String, Long>> monthlyData = projections.stream()
                .collect(Collectors.groupingBy(
                        EarningReportProjection::getMonth,
                        LinkedHashMap::new,
                        Collectors.toMap(
                                proj -> proj.getPaymentMode() != null ? proj.getPaymentMode() : "Unknown",
                                EarningReportProjection::getTransactionCount,
                                (a, b) -> a + b // Merge function if duplicate keys
                        )
                ));

        return monthlyData.entrySet().stream()
                .map(entry -> EarningReportDto.builder()
                        .month(entry.getKey())
                        .paymentModeCount(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<LatestTransactionDto> getLatestTransactions(Long companyId, int limit) {
        List<LatestTransactionProjection> projections = dashboardRepository.getLatestTransactions(companyId, limit);

        if (projections == null) {
            return Collections.emptyList();
        }

        return projections.stream()
                .map(proj -> LatestTransactionDto.builder()
                        .paymentMode(proj.getPaymentMode())
                        .crDrIndicator(proj.getCrDrIndicator())
                        .transDate(proj.getTransDate())
                        .bankName(proj.getBankName())
                        .tranAmount(proj.getTranAmount())
                        .clientName(proj.getClientName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TopClientDto> getTopClients(Long companyId, int limit) {
        List<TopClientProjection> projections = dashboardRepository.getTopClients(companyId, limit);

        if (projections == null) {
            return Collections.emptyList();
        }

        return projections.stream()
                .map(proj -> {
                    double percentageChange = calculatePercentageChange(
                            proj.getCurrentAmount(),
                            proj.getPreviousAmount()
                    );
                    return TopClientDto.builder()
                            .clientId(proj.getClientId())
                            .clientName(proj.getClientName())
                            .currentMonthAmount(proj.getCurrentAmount())
                            .twoMonthsAgoAmount(proj.getPreviousAmount())
                            .percentageChange(percentageChange)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Helper method to build a metric with percentage change and trend
    private DashboardWidgetDto buildMetric(Double currentValue, Double previousValue) {
        double change = calculatePercentageChange(currentValue, previousValue);
        return DashboardWidgetDto.builder()
                .currentValue(currentValue)
                .previousValue(previousValue)
                .percentageChange(Math.abs(change))
                .trend(change >= 0 ? "up" : "down")
                .build();
    }

    // Helper method to build an empty metric
    private DashboardWidgetDto buildEmptyMetric() {
        return DashboardWidgetDto.builder()
                .currentValue(0.0)
                .previousValue(0.0)
                .percentageChange(0.0)
                .trend("up")
                .build();
    }

    // Helper method to calculate percentage change, handling divide by zero
    private double calculatePercentageChange(Double current, Double previous) {
        if (current == null) current = 0.0;
        if (previous == null || previous == 0.0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((current - previous) * 100.0) / previous;
    }
}
