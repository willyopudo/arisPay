package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardWidgetsDto {
    private DashboardWidgetDto collections;
    private DashboardWidgetDto disbursements;
    private DashboardWidgetDto totalCollections;
    private DashboardWidgetDto totalDisbursements;
}
