package org.arispay.utils;

import okhttp3.Credentials;
import okhttp3.Request;
import org.arispay.adapters.auth.*;
import org.arispay.infraconfig.PartnerAuthProperties;
import org.arispay.ports.api.AuthStrategyServicePort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class AuthStrategyResolver {

    private final PartnerAuthProperties partnerAuthProperties;
    private final OAuth2TokenService oAuth2TokenService;
    private final Map<String, Pattern> urlPatternCache = new ConcurrentHashMap<>();

    public AuthStrategyResolver(PartnerAuthProperties partnerAuthProperties,
                                OAuth2TokenService oAuth2TokenService) {
        this.partnerAuthProperties = partnerAuthProperties;
        this.oAuth2TokenService = oAuth2TokenService;
    }

    // Existing Spring HttpHeaders method
    public AuthStrategyServicePort resolve(String url) {
        PartnerAuthProperties.PartnerConfig config = getConfigForUrl(url);
        if (config != null) {
            return createAuthStrategy(config);
        }
        throw new UnsupportedOperationException("No auth strategy configured for URL: " + url);
    }

    // New method to get raw config for OkHttp strategies
    public PartnerAuthProperties.PartnerConfig getConfigForUrl(String url) {
        for (PartnerAuthProperties.PartnerConfig config : partnerAuthProperties.getConfigs()) {
            if (matchesUrlPattern(url, config.getUrlPattern())) {
                return config;
            }
        }
        return null;
    }

    // OkHttp-specific method that applies auth directly to Request
    public Request applyAuthToRequest(Request originalRequest) {
        String url = originalRequest.url().toString();
        PartnerAuthProperties.PartnerConfig config = getConfigForUrl(url);

        if (config == null) {
            return originalRequest; // No auth needed
        }

        return applyConfigToRequest(originalRequest, config);
    }

    private Request applyConfigToRequest(Request originalRequest, PartnerAuthProperties.PartnerConfig config) {
        Request.Builder builder = originalRequest.newBuilder();

        switch (config.getStrategy()) {
            case OAUTH2_CLIENT_CREDENTIALS:
            case OPENID_CONNECT:
            case CUSTOM_JWT:
                // Get token from OAuth2 service (handles caching automatically)
                String oauthToken = oAuth2TokenService.getAccessToken(config);
                return builder.header("Authorization", "Bearer " + oauthToken).build();

            case BASIC:
                String credentials = Credentials.basic(
                        config.getBasic().getUsername(),
                        config.getBasic().getPassword()
                );
                return builder.header("Authorization", credentials).build();

            case API_KEY:
                return builder.header(
                        config.getApiKey().getHeaderName(),
                        config.getApiKey().getKey()
                ).build();

            case NONE:
            default:
                return originalRequest; // No auth modifications
        }
    }

    private boolean matchesUrlPattern(String url, String pattern) {
        Pattern compiledPattern = urlPatternCache.computeIfAbsent(pattern, Pattern::compile);
        return compiledPattern.matcher(url).matches();
    }

    private AuthStrategyServicePort createAuthStrategy(PartnerAuthProperties.PartnerConfig config) {
        switch (config.getStrategy()) {
            case OAUTH2_CLIENT_CREDENTIALS:
            case OPENID_CONNECT:
            case CUSTOM_JWT:
                return new OAuth2AuthStrategy(oAuth2TokenService, config);
            case BASIC:
                return new BasicAuthStrategy(
                        config.getBasic().getUsername(),
                        config.getBasic().getPassword()
                );
            case API_KEY:
                return new ApiKeyAuthStrategy(
                        config.getApiKey().getHeaderName(),
                        config.getApiKey().getKey()
                );
            case NONE:
                return new NoAuthStrategy();
            default:
                throw new IllegalArgumentException("Unsupported auth strategy: " + config.getStrategy());
        }
    }
}
