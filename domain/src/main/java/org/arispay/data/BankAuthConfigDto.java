package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAuthConfigDto {
    private Long id;
    private String bankCode;
    private String partnerName;
    private String urlPattern;
    private String authUrl;
    private String strategy;
    private String clientId;
    private String clientSecret;
    private String scope;
    private String bodyFormat;
    private long tokenCacheDuration;
    private boolean active;
}
