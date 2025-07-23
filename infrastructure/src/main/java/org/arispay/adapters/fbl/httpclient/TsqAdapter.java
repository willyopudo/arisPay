package org.arispay.adapters.fbl.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.arispay.data.TransactionDto;
import org.arispay.data.fbl.dtoresponse.tsq.FblTsqResponse;
import org.arispay.ports.spi.httpclient.TsqHttpClientPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Service("070")
public class TsqAdapter implements TsqHttpClientPort {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization/deserialization
    private final String tsqBaseUrl = "http://localhost:8085/api/v1"; // Replace with actual TSQ service URL

    public TsqAdapter(@Qualifier("okHttpClient") OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public TransactionDto queryTransaction(String bankRef) {
        String url = tsqBaseUrl + "/demo/transactions/" + bankRef;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //Make an HTTP call to the TSQ service using the bankRef

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if(response.code() == 404) {
                    throw new NoSuchElementException("No transaction found for transaction reference: " + bankRef);
                }
                throw new RuntimeException("Request failed: " + response.code());
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            FblTsqResponse fblTsqResponse = objectMapper.readValue(responseBody, FblTsqResponse.class);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
            System.out.println("FBL TSQ Response: " + fblTsqResponse.toString());
            return new TransactionDto(
                    0L, // Assuming ID is auto-generated or not needed for this response
                    fblTsqResponse.transRef(),
                    null,
                    fblTsqResponse.amount(),
                    fblTsqResponse.accountNumber(),
                    "070", // Bank code for FBL
                    1L,
                    null, // Assuming company ID is not needed for this response
                    fblTsqResponse.payerName(),
                    fblTsqResponse.payerPhone(),
                    fblTsqResponse.paymentMode(), //Todo: handle if not passed or null
                    fblTsqResponse.narration(),
                    fblTsqResponse.channel(),
                    LocalDateTime.parse(fblTsqResponse.transDate(), formatter),
                    fblTsqResponse.transType(),
                    null
            );
        }
        catch (NoSuchElementException e) {
            throw e; // Propagate the exception for handling upstream
        }
        catch (Exception e) {
            throw new RuntimeException("Error querying TSQ service", e);
        }
    }
}
