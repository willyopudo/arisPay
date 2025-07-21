package org.arispay.data.dtoauth;

import lombok.Data;

// Cached token holder
@Data
public class CachedToken {
    private String accessToken;
    private long expirationTime;

    public CachedToken(String accessToken, long cacheDurationSeconds) {
        this.accessToken = accessToken;
        this.expirationTime = System.currentTimeMillis() + (cacheDurationSeconds * 1000);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expirationTime;
    }

    public boolean isExpiredWithBuffer(int bufferSeconds) {
        return System.currentTimeMillis() >= (expirationTime - (bufferSeconds * 1000L));
    }
}
