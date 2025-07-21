package org.arispay.adapters.auth;

import org.arispay.infraconfig.PartnerAuthProperties;
import org.arispay.ports.api.AuthStrategyServicePort;
import org.springframework.http.HttpHeaders;

public class OAuth2AuthStrategy implements AuthStrategyServicePort {
    private final OAuth2TokenService tokenService;
    private final PartnerAuthProperties.PartnerConfig config;

    public OAuth2AuthStrategy(OAuth2TokenService tokenService, PartnerAuthProperties.PartnerConfig config) {
        this.tokenService = tokenService;
        this.config = config;
    }

    @Override
    public void applyAuth(HttpHeaders headers) {
        String token = tokenService.getAccessToken(config);
        headers.setBearerAuth(token);
    }

    @Override
    public String getStrategyName() {
        return config.getStrategy().name();
    }
}
