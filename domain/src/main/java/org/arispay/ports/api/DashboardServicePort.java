package org.arispay.ports.api;

import org.arispay.data.*;

import java.util.List;

public interface DashboardServicePort {
    DashboardWidgetsDto getWidgets(Long companyId);
    List<EarningReportDto> getEarningReports(Long companyId, int months);
    List<LatestTransactionDto> getLatestTransactions(Long companyId, int limit);
    List<TopClientDto> getTopClients(Long companyId, int limit);
    DashboardSummaryDto getDashboardSummary(Long companyId);
}
