package org.arispay.ports.spi;

import org.arispay.data.*;

import java.util.List;

public interface DashboardPersistencePort {
    DashboardWidgetsDto getWidgets(Long companyId);
    List<EarningReportDto> getEarningReports(Long companyId, int months);
    List<LatestTransactionDto> getLatestTransactions(Long companyId, int limit);
    List<TopClientDto> getTopClients(Long companyId, int limit);
}
