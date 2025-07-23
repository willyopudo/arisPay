package org.arispay.adapters.auth;

import org.arispay.ports.api.AuthStrategyServicePort;
import org.springframework.http.HttpHeaders;

public class NoAuthStrategy implements AuthStrategyServicePort {
    @Override
    public void applyAuth(HttpHeaders headers) {
        // No authentication needed
    }

    @Override
    public String getStrategyName() {
        return "NONE";
    }
}
