package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardSummaryDto {
    private DashboardWidgetsDto widgets;
    private List<EarningReportDto> earningReports;
    private List<LatestTransactionDto> latestTransactions;
    private List<TopClientDto> topClients;
}
