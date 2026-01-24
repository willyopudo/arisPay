package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EarningReportDto {
    private String month; // e.g., "Jan", "Feb"
    private Map<String, Long> paymentModeCount; // {"CASH": 10, "CARD": 5}
}
