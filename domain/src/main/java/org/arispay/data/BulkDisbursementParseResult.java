package org.arispay.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkDisbursementParseResult {
    private List<DisbursementItemDto> items;
    private List<String> errors;
    private int totalRecords;
    private double totalAmount;
    private boolean valid;
}
