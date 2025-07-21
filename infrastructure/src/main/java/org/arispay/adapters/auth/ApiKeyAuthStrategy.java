package org.arispay.adapters.auth;

import org.arispay.ports.api.AuthStrategyServicePort;
import org.springframework.http.HttpHeaders;

public class ApiKeyAuthStrategy implements AuthStrategyServicePort {
    private final String headerName;
    private final String apiKey;

    public ApiKeyAuthStrategy(String headerName, String apiKey) {
        this.headerName = headerName;
        this.apiKey = apiKey;
    }

    @Override
    public void applyAuth(HttpHeaders headers) {
        headers.set(headerName, apiKey);
    }

    @Override
    public String getStrategyName() {
        return "API_KEY";
    }
}
