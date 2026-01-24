package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardWidgetDto {
    private Double currentValue;
    private Double previousValue;
    private Double percentageChange;
    private String trend; // "up" or "down"
}
