package org.arispay.ports.api;
import org.springframework.http.HttpHeaders;

public interface AuthStrategyServicePort {
    void applyAuth(HttpHeaders headers);
    String getStrategyName();
}
