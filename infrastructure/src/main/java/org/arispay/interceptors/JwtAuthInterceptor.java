package org.arispay.interceptors;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.arispay.utils.JwtTokenProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements Interceptor {

    private final JwtTokenProvider tokenProvider;

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String jwt = tokenProvider.currentToken();   // fresh or cached token
        Request authorised = chain.request()
                .newBuilder()
                .header("Authorization", "Bearer " + jwt)
                .build();
        return chain.proceed(authorised);
    }
}
