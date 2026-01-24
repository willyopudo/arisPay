package org.arispay.adapters.auth;

import org.arispay.ports.api.AuthStrategyServicePort;
import org.springframework.http.HttpHeaders;

public class BasicAuthStrategy implements AuthStrategyServicePort {
    private final String username;
    private final String password;

    public BasicAuthStrategy(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void applyAuth(HttpHeaders headers) {
        headers.setBasicAuth(username, password);
    }

    @Override
    public String getStrategyName() {
        return "BASIC";
    }
}
