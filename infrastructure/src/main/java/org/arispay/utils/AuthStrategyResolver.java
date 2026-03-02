package org.arispay.utils;

import okhttp3.Credentials;
import okhttp3.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.adapters.auth.*;
import org.arispay.data.BankAuthConfigDto;
import org.arispay.enums.AuthStrategyType;
import org.arispay.infraconfig.PartnerAuthProperties;
import org.arispay.ports.api.AuthStrategyServicePort;
import org.arispay.ports.spi.BankAuthConfigPersistencePort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class AuthStrategyResolver {

    private static final Logger logger = LogManager.getLogger(AuthStrategyResolver.class);

    private final PartnerAuthProperties partnerAuthProperties;
    private final OAuth2TokenService oAuth2TokenService;
    private final BankAuthConfigPersistencePort bankAuthConfigPersistencePort;
    private final Map<String, Pattern> urlPatternCache = new ConcurrentHashMap<>();
    private volatile List<PartnerAuthProperties.PartnerConfig> mergedConfigs;

    public AuthStrategyResolver(PartnerAuthProperties partnerAuthProperties,
                                OAuth2TokenService oAuth2TokenService,
                                BankAuthConfigPersistencePort bankAuthConfigPersistencePort) {
        this.partnerAuthProperties = partnerAuthProperties;
        this.oAuth2TokenService = oAuth2TokenService;
        this.bankAuthConfigPersistencePort = bankAuthConfigPersistencePort;
    }

    // Existing Spring HttpHeaders method
    public AuthStrategyServicePort resolve(String url) {
        PartnerAuthProperties.PartnerConfig config = getConfigForUrl(url);
        if (config != null) {
            return createAuthStrategy(config);
        }
        throw new UnsupportedOperationException("No auth strategy configured for URL: " + url);
    }

    // Method to get raw config for OkHttp strategies - checks both DB and YAML
    public PartnerAuthProperties.PartnerConfig getConfigForUrl(String url) {
        List<PartnerAuthProperties.PartnerConfig> allConfigs = getMergedConfigs();
        for (PartnerAuthProperties.PartnerConfig config : allConfigs) {
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

    /**
     * Lazily loads and merges DB-backed bank auth configs with YAML configs.
     * DB configs take precedence (loaded first).
     */
    private List<PartnerAuthProperties.PartnerConfig> getMergedConfigs() {
        if (mergedConfigs == null) {
            synchronized (this) {
                if (mergedConfigs == null) {
                    mergedConfigs = buildMergedConfigs();
                }
            }
        }
        return mergedConfigs;
    }

    private List<PartnerAuthProperties.PartnerConfig> buildMergedConfigs() {
        List<PartnerAuthProperties.PartnerConfig> configs = new ArrayList<>();

        // Load DB-backed configs first (higher priority)
        try {
            List<BankAuthConfigDto> dbConfigs = bankAuthConfigPersistencePort.getAllActiveConfigs();
            for (BankAuthConfigDto dbConfig : dbConfigs) {
                PartnerAuthProperties.PartnerConfig partnerConfig = toPartnerConfig(dbConfig);
                configs.add(partnerConfig);
                logger.info("Loaded DB auth config for partner: {}", dbConfig.getPartnerName());
            }
        } catch (Exception e) {
            logger.warn("Failed to load DB auth configs, falling back to YAML only: {}", e.getMessage());
        }

        // Add YAML configs (lower priority - won't override DB if same URL pattern matches)
        configs.addAll(partnerAuthProperties.getConfigs());

        return configs;
    }

    private PartnerAuthProperties.PartnerConfig toPartnerConfig(BankAuthConfigDto dbConfig) {
        PartnerAuthProperties.PartnerConfig config = new PartnerAuthProperties.PartnerConfig();
        config.setName(dbConfig.getPartnerName());
        config.setUrlPattern(dbConfig.getUrlPattern());
        config.setAuthUrl(dbConfig.getAuthUrl());
        config.setStrategy(AuthStrategyType.valueOf(dbConfig.getStrategy()));

        // Build OAuth2Config from DB fields
        PartnerAuthProperties.PartnerConfig.OAuth2Config oauth2 = new PartnerAuthProperties.PartnerConfig.OAuth2Config();
        oauth2.setClientId(dbConfig.getClientId());
        oauth2.setClientSecret(dbConfig.getClientSecret());
        oauth2.setScope(dbConfig.getScope());
        oauth2.setBodyFormat(dbConfig.getBodyFormat());
        oauth2.setTokenCacheDuration(dbConfig.getTokenCacheDuration());
        config.setOauth2(oauth2);

        return config;
    }

    /**
     * Clears merged config cache so next call re-reads from DB.
     * Call this after admin updates to bank_auth_config.
     */
    public void refreshConfigs() {
        synchronized (this) {
            mergedConfigs = null;
            urlPatternCache.clear();
        }
        logger.info("Auth strategy configs refreshed");
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
