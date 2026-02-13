package org.arispay.data.fbl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkPostingResult {
    private String accountDr;
    private int detailCount;
    private double totalAmount;
}
