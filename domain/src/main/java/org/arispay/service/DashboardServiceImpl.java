package org.arispay.service;

import org.arispay.data.*;
import org.arispay.ports.api.DashboardServicePort;
import org.arispay.ports.spi.DashboardPersistencePort;

import java.util.List;

public class DashboardServiceImpl implements DashboardServicePort {

    private final DashboardPersistencePort dashboardPersistencePort;

    public DashboardServiceImpl(DashboardPersistencePort dashboardPersistencePort) {
        this.dashboardPersistencePort = dashboardPersistencePort;
    }

    @Override
    public DashboardWidgetsDto getWidgets(Long companyId) {
        return dashboardPersistencePort.getWidgets(companyId);
    }

    @Override
    public List<EarningReportDto> getEarningReports(Long companyId, int months) {
        return dashboardPersistencePort.getEarningReports(companyId, months);
    }

    @Override
    public List<LatestTransactionDto> getLatestTransactions(Long companyId, int limit) {
        return dashboardPersistencePort.getLatestTransactions(companyId, limit);
    }

    @Override
    public List<TopClientDto> getTopClients(Long companyId, int limit) {
        return dashboardPersistencePort.getTopClients(companyId, limit);
    }

    @Override
    public DashboardSummaryDto getDashboardSummary(Long companyId) {
        return DashboardSummaryDto.builder()
                .widgets(getWidgets(companyId))
                .earningReports(getEarningReports(companyId, 12))
                .latestTransactions(getLatestTransactions(companyId, 5))
                .topClients(getTopClients(companyId, 6))
                .build();
    }
}
