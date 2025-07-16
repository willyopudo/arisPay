package org.arispay.adapters.fbl.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.arispay.data.TransactionDto;
import org.arispay.ports.spi.httpclient.TsqHttpClientPort;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("070")
public class TsqAdapter implements TsqHttpClientPort {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization/deserialization
    private final String tsqBaseUrl = "https://api.tsq.example.com/v1"; // Replace with actual TSQ service URL

    public TsqAdapter(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public TransactionDto queryTransaction(String bankRef) {
        String url = tsqBaseUrl + "/transactions/" + bankRef;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //Make an HTTP call to the TSQ service using the bankRef

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Request failed: " + response.code());
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, TransactionDto.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Error querying TSQ service", e);
        }
    }
}
