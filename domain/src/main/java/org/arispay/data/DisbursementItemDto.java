package org.arispay.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisbursementItemDto {
    private String beneficiaryName;
    private String beneficiaryAccount;
    private String beneficiaryBank;
    private double amount;
    private String purpose;
    private String remarks;
    private String paymentType;
    private String currency;
}
