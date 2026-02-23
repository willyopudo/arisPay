package org.arispay.adapters.disbursement;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.ports.api.BankDisbursementServicePort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FamilyBankDisbursementAdapter implements BankDisbursementServicePort {

    private static final Logger logger = LogManager.getLogger(FamilyBankDisbursementAdapter.class);
    private static final String BANK_CODE = "FBL";
    private static final String BULK_PAY_URL = "https://openbank.bankabc.com/api/v1/Transaction";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FamilyBankDisbursementAdapter(@Qualifier("okHttpClient") OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String getBankCode() {
        return BANK_CODE;
    }

    @Override
    public BulkTransactionResponse processBulkDisbursement(BulkTransactionRequest request) {
        try {
            String jsonBody = objectMapper.writeValueAsString(request);

            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

            Request httpRequest = new Request.Builder()
                    .url(BULK_PAY_URL)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    logger.error("Family Bank bulk disbursement failed with status: {}", response.code());
                    throw new RuntimeException("Family Bank bulk disbursement failed: " + response.code());
                }

                assert response.body() != null;
                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, BulkTransactionResponse.class);
            }
        } catch (Exception e) {
            logger.error("Error processing Family Bank bulk disbursement: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing Family Bank bulk disbursement", e);
        }
    }
}
