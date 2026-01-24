package org.arispay.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.ports.api.AuthStrategyServicePort;
import org.arispay.utils.AuthStrategyResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public class AdaptiveAuthInterceptor implements Interceptor {

    private final AuthStrategyResolver authStrategyResolver;
    private static final Logger logger = LogManager.getLogger(AdaptiveAuthInterceptor.class);

    public AdaptiveAuthInterceptor(AuthStrategyResolver authStrategyResolver) {
        this.authStrategyResolver = authStrategyResolver;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String url = originalRequest.url().toString();

        try {
            // Resolve auth strategy based on URL
            AuthStrategyServicePort authStrategy = authStrategyResolver.resolve(url);

            // Create new request with authentication applied
            Request authenticatedRequest = applyAuthStrategy(originalRequest, authStrategy);

            logger.debug("Applying {} auth strategy for URL: {}", authStrategy.getStrategyName(), url);

            return chain.proceed(authenticatedRequest);

        } catch (UnsupportedOperationException e) {
            // No auth strategy found, proceed with original request
            logger.debug("No auth strategy found for URL: {}, proceeding without authentication", url);
            return chain.proceed(originalRequest);
        }
    }

    private Request applyAuthStrategy(Request originalRequest, AuthStrategyServicePort authStrategy) {
        Request.Builder requestBuilder = originalRequest.newBuilder();

        // Convert Spring's HttpHeaders to OkHttp headers
        HttpHeaders springHeaders = new HttpHeaders();
        authStrategy.applyAuth(springHeaders);

        // Apply headers to OkHttp request
        springHeaders.forEach((headerName, headerValues) -> {
            headerValues.forEach(headerValue -> {
                requestBuilder.header(headerName, headerValue);
            });
        });

        return requestBuilder.build();
    }
}