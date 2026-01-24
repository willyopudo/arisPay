package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LatestTransactionDto {
    private String paymentMode;
    private String crDrIndicator;
    private LocalDateTime transDate;
    private String bankName;
    private Double tranAmount;
    private String clientName; // optional - can be null
}
