package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopClientDto {
    private Long clientId;
    private String clientName;
    private Double currentMonthAmount;
    private Double twoMonthsAgoAmount;
    private Double percentageChange;
}
