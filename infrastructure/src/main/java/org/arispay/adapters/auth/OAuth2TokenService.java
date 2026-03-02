package org.arispay.adapters.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.dtoauth.CachedToken;
import org.arispay.data.dtoauth.CustomJwtTokenResponse;
import org.arispay.data.dtoauth.OAuth2TokenResponse;
import org.arispay.data.dtoauth.OpenIdTokenResponse;
import org.arispay.infraconfig.PartnerAuthProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OAuth2TokenService {
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Map<String, CachedToken> tokenCache = new ConcurrentHashMap<>();
    private static final Logger logger = LogManager.getLogger(OAuth2TokenService.class);

    public OAuth2TokenService(ObjectMapper objectMapper) {
        // Create a separate HTTP client without auth interceptor to avoid circular dependency
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = objectMapper;
    }

    public String getAccessToken(PartnerAuthProperties.PartnerConfig config) {
        String cacheKey = config.getName();
        CachedToken cachedToken = tokenCache.get(cacheKey);

        // Check if token is still valid (with 1 minute buffer)
        if (cachedToken != null && !cachedToken.isExpiredWithBuffer(60)) {
            logger.debug("Using cached token for partner: {}", config.getName());
            return cachedToken.getAccessToken();
        }

        // Fetch new token
        try {
            String newToken = fetchNewToken(config);
            long cacheDuration = getCacheDuration(config);
            tokenCache.put(cacheKey, new CachedToken(newToken, cacheDuration));
            logger.info("Fetched new access token for partner: {}", config.getName());
            return newToken;
        } catch (Exception e) {
            logger.error("Failed to fetch access token for partner: {}", config.getName(), e);
            throw new RuntimeException("Failed to get access token for " + config.getName(), e);
        }
    }

    private String fetchNewToken(PartnerAuthProperties.PartnerConfig config) throws IOException {
        return switch (config.getStrategy()) {
            case OAUTH2_CLIENT_CREDENTIALS -> fetchOAuth2Token(config);
            case OPENID_CONNECT -> fetchOpenIdToken(config);
            case CUSTOM_JWT -> fetchCustomJwtToken(config);
            default -> throw new IllegalArgumentException("Unsupported token strategy: " + config.getStrategy());
        };
    }

    private String fetchOAuth2Token(PartnerAuthProperties.PartnerConfig config) throws IOException {
        PartnerAuthProperties.PartnerConfig.OAuth2Config oauth2Config = config.getOauth2();
        String bodyFormat = oauth2Config.getBodyFormat();

        RequestBody requestBody;
        String contentType;

        if ("JSON".equalsIgnoreCase(bodyFormat)) {
            // Send as JSON body
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("grant_type", "client_credentials");
            jsonMap.put("client_id", oauth2Config.getClientId());
            jsonMap.put("client_secret", oauth2Config.getClientSecret());
            if (oauth2Config.getScope() != null && !oauth2Config.getScope().isEmpty()) {
                jsonMap.put("scope", oauth2Config.getScope());
            }
            String jsonBody = objectMapper.writeValueAsString(jsonMap);
            requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));
            contentType = "application/json";
        } else {
            // Default: send as form-urlencoded
            FormBody.Builder formBuilder = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .add("client_id", oauth2Config.getClientId())
                    .add("client_secret", oauth2Config.getClientSecret());

            if (oauth2Config.getScope() != null && !oauth2Config.getScope().isEmpty()) {
                formBuilder.add("scope", oauth2Config.getScope());
            }
            requestBody = formBuilder.build();
            contentType = "application/x-www-form-urlencoded";
        }

        Request request = new Request.Builder()
                .url(config.getAuthUrl())
                .post(requestBody)
                .header("Content-Type", contentType)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("OAuth2 token request failed: " + response.code() + " " + response.message());
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            OAuth2TokenResponse tokenResponse = objectMapper.readValue(responseBody, OAuth2TokenResponse.class);
            return tokenResponse.getAccessToken();
        }
    }

    private String fetchOpenIdToken(PartnerAuthProperties.PartnerConfig config) throws IOException {
        PartnerAuthProperties.PartnerConfig.OpenIdConfig openidConfig = config.getOpenid();
        String bodyFormat = openidConfig.getBodyFormat();

        RequestBody requestBody;
        String contentType;

        if ("JSON".equalsIgnoreCase(bodyFormat)) {
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("grant_type", "client_credentials");
            jsonMap.put("client_id", openidConfig.getClientId());
            jsonMap.put("client_secret", openidConfig.getClientSecret());
            jsonMap.put("scope", openidConfig.getScope());
            String jsonBody = objectMapper.writeValueAsString(jsonMap);
            requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));
            contentType = "application/json";
        } else {
            requestBody = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .add("client_id", openidConfig.getClientId())
                    .add("client_secret", openidConfig.getClientSecret())
                    .add("scope", openidConfig.getScope())
                    .build();
            contentType = "application/x-www-form-urlencoded";
        }

        Request request = new Request.Builder()
                .url(config.getAuthUrl())
                .post(requestBody)
                .header("Content-Type", contentType)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("OpenID token request failed: " + response.code());
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            OpenIdTokenResponse tokenResponse = objectMapper.readValue(responseBody, OpenIdTokenResponse.class);
            return tokenResponse.getAccessToken();
        }
    }

    private String fetchCustomJwtToken(PartnerAuthProperties.PartnerConfig config) throws IOException {
        // Custom JWT authentication - send credentials to get JWT
        PartnerAuthProperties.PartnerConfig.CustomJwtConfig customConfig = config.getCustomJwt();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", customConfig.getUsername());
        requestBody.put("password", customConfig.getPassword());
        requestBody.putAll(customConfig.getAdditionalClaims());

        String jsonBody = objectMapper.writeValueAsString(requestBody);
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(config.getAuthUrl())
                .post(body)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Custom JWT token request failed: " + response.code());
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            CustomJwtTokenResponse tokenResponse = objectMapper.readValue(responseBody, CustomJwtTokenResponse.class);
            return tokenResponse.getToken();
        }
    }

    private long getCacheDuration(PartnerAuthProperties.PartnerConfig config) {
        return switch (config.getStrategy()) {
            case OAUTH2_CLIENT_CREDENTIALS -> config.getOauth2().getTokenCacheDuration();
            case OPENID_CONNECT -> config.getOpenid().getTokenCacheDuration();
            case CUSTOM_JWT -> config.getCustomJwt().getTokenCacheDuration();
            default -> 3300; // 55 minutes default
        };
    }

    // Clear cache for specific partner (useful for testing or manual refresh)
    public void clearTokenCache(String partnerName) {
        tokenCache.remove(partnerName);
        logger.info("Cleared token cache for partner: {}", partnerName);
    }

    // Clear all cached tokens
    public void clearAllTokenCache() {
        tokenCache.clear();
        logger.info("Cleared all token cache");
    }
}
