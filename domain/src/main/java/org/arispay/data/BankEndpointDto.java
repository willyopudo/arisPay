package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankEndpointDto {
    private Long id;
    private String bankCode;
    private String endpointName;
    private String endpointUrl;
    private String description;
    private boolean active;
}
