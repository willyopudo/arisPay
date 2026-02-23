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
public class BulkDisbursementUploadDto {
    private String bankCode;
    private String accountDr;
    private String narration;
    private String currency;
    private List<DisbursementItemDto> items;
}
