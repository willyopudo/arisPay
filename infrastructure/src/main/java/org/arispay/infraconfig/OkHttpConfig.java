package org.arispay.infraconfig;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.arispay.interceptors.JwtAuthInterceptor;
import org.arispay.interceptors.RetryInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {

    @Value("${partners.timeout.connect:5000}")   // ms
    private int connectT;
    @Value("${partners.timeout.read:5000}")
    private int readT;
    @Value("${partners.timeout.write:5000}")
    private int writeT;

    @Bean
    public OkHttpClient okHttpClient(JwtAuthInterceptor jwt) {
        // connection pool: 100 idle connections, keep‑alive 5 min
        ConnectionPool pool = new ConnectionPool(100, 5, TimeUnit.MINUTES);

        // log every request + response headers
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .connectionPool(pool)
                .connectTimeout(connectT, TimeUnit.MILLISECONDS)
                .readTimeout(readT, TimeUnit.MILLISECONDS)
                .writeTimeout(writeT, TimeUnit.MILLISECONDS) // timeouts :contentReference[oaicite:1]{index=1}
                .retryOnConnectionFailure(true)
                .addInterceptor(new RetryInterceptor(3, 500)) // retry 3 times with exponential backoff
                .addInterceptor(log) // logging interceptor
                .addInterceptor(jwt)     // JWT auth interceptor
                .build();
    }
}
