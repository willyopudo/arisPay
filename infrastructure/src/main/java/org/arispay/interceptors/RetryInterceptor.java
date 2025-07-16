package org.arispay.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {
    private final int maxRetries;
    private final long initialBackoffMillis;

    public RetryInterceptor(int maxRetries, long initialBackoffMillis) {
        this.maxRetries = maxRetries;
        this.initialBackoffMillis = initialBackoffMillis;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        int tryCount = 0;
        IOException lastException = null;
        long backoff = initialBackoffMillis;

        while (tryCount < maxRetries) {
            try {
                return chain.proceed(request);
            } catch (IOException e) {
                lastException = e;
                tryCount++;
                if (tryCount >= maxRetries) break;
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Retry interrupted", ie);
                }
                backoff *= 2; // Exponential backoff
            }
        }
        assert lastException != null;
        throw lastException;
    }
}
