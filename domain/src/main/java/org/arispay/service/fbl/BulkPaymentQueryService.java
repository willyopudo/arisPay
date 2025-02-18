package org.arispay.service.fbl;

import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
public class BulkPaymentQueryService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BulkTransactionPersistencePort bulkTransactionPersistencePort;

    @Autowired
    private TokenService tokenService;

    @Scheduled(cron = "*/5 * * * * *")
    private void queryBulkTransactionStatus() {
        try {

            List<String> batchRefs = bulkTransactionPersistencePort.queryBulkTransactions(LocalDateTime.now(), 5);

            for(String batchRef: batchRefs) {

                bulkTransactionPersistencePort.markProcessingStage(Long.parseLong(batchRef.replace("BULK70", "")),"S");
                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                headers.add("Authorization", "Bearer " + tokenService.getAccessToken());
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<?> httpEntity = new HttpEntity<>(headers);

                String uri = "https://openbank.bankabc.com/api/v1/Transaction";

                String urlTemplate = UriComponentsBuilder.fromHttpUrl(uri)
                        .queryParam("BatchREF", "{BatchREF}")
                        .encode()
                        .toUriString();

                Map<String, String> params = new HashMap<>();
                params.put("BatchREF", batchRef);

                ResponseEntity<BulkTransactionResponse> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET,
                        httpEntity,
                        BulkTransactionResponse.class,
                        params);

                BulkTransactionResponse response = responseEntity.getBody();
                bulkTransactionPersistencePort.updateBulkTransaction(response, "P");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
