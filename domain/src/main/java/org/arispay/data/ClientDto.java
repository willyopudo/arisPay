package org.arispay.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {
    private Long id;
    private String clientName;
    private String clientId;
    private String identifierType;
    private Long company;
    private String companyName;
    @JsonProperty("status")
    private byte isEnabled;
    private LocalDateTime createdDate;
}
