package org.arispay.infraconfig;

import lombok.Data;
import org.arispay.enums.AuthStrategyType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// PartnerAuthProperties.java
@ConfigurationProperties(prefix = "partners.auth")
@Component
@Data
public class PartnerAuthProperties {
    private List<PartnerConfig> configs = new ArrayList<>();

    @Data
    public static class PartnerConfig {
        private String name;
        private String urlPattern;
        private String authUrl;  // auth endpoint for token acquisition
        private AuthStrategyType strategy;
        private JwtConfig jwt;
        private BasicConfig basic;
        private ApiKeyConfig apiKey;
        private OAuth2Config oauth2;  // OAuth2 client credentials
        private OpenIdConfig openid;  // OpenID Connect
        private CustomJwtConfig customJwt;  // Custom JWT with auth endpoint

        @Data
        public static class JwtConfig {
            private String secret;
            private long expiration;
        }

        @Data
        public static class BasicConfig {
            private String username;
            private String password;
        }

        @Data
        public static class ApiKeyConfig {
            private String headerName;
            private String key;
        }

        @Data
        public static class OAuth2Config {
            private String clientId;
            private String clientSecret;
            private String scope;
            private long tokenCacheDuration = 3300; // 55 minutes default
        }

        @Data
        public static class OpenIdConfig {
            private String clientId;
            private String clientSecret;
            private String scope;
            private long tokenCacheDuration = 3300;
        }

        @Data
        public static class CustomJwtConfig {
            private String username;
            private String password;
            private Map<String, String> additionalClaims = new HashMap<>();
            private long tokenCacheDuration = 1800; // 30 minutes default
        }
    }
}

