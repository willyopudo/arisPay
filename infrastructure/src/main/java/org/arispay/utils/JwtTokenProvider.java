package org.arispay.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.arispay.data.dtoauth.ExternalJwtRequest;
import org.arispay.data.dtoauth.ExternalJwtResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtTokenProvider {

    private final OkHttpClient authClient;          // or WebClient/RestTemplate
    private final String authUrl;                   // injected from config
    private final Object lock = new Object();       // prevent stampede
    private volatile TokenHolder cached;            // token + expiry

    public JwtTokenProvider(
            @Qualifier("authOkHttpClient") OkHttpClient authClient,
            @Value("${custom.arispay.partners.family.auth.url}") String authUrl) {
        this.authClient = authClient;
        this.authUrl = authUrl;
    }

    public String currentToken() throws IOException {
        // Always fetch if you really need a fresh token each call.
        // Otherwise cache until just‑before expiry to save round‑trips.
        if (cached == null || cached.expiresSoon()) {
            synchronized (lock) {           // single refresh at a time :contentReference[oaicite:0]{index=0}
                if (cached == null || cached.expiresSoon()) {
                    cached = fetchNewToken();
                }
            }
        }
        return cached.token();
    }

    private TokenHolder fetchNewToken() throws IOException {
        ExternalJwtRequest requestObj = new ExternalJwtRequest("CL0981829", "FKFKJFIHUE", "client_credentials","arispay:read");
        String json = new ObjectMapper().writeValueAsString(requestObj);
        RequestBody body = RequestBody.create(json, okhttp3.MediaType.parse("application/json"));

        // Make the HTTP call to the auth service
        Request request = new Request.Builder()
                .url(authUrl)
                .post(body)
                .build();
        try (Response resp = authClient.newCall(request).execute()) {
            if (!resp.isSuccessful()) throw new IOException("Auth failed");
            assert resp.body() != null;
            ExternalJwtResponse dto = new ObjectMapper()
                    .readValue(resp.body().string(), ExternalJwtResponse.class);
            return new TokenHolder(dto.accessToken(), Instant.now().plusSeconds(dto.expiresIn()));
        }
    }

    private record TokenHolder(String token, Instant exp) {
        boolean expiresSoon() { return exp.isBefore(Instant.now().plusSeconds(30)); }
    }
}
