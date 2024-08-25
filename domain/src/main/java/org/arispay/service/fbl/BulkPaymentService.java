package org.arispay.service.fbl;

import java.net.URI;
import java.net.URISyntaxException;
import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.ports.spi.fbl.BulkTransactionPersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BulkPaymentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BulkTransactionPersistencePort bulkTransactionPersistencePort;
    
    @Autowired
    private TokenService tokenService;

    public BulkTransactionResponse initiateBulkTransactionRequest(BulkTransactionRequest bulkTransactionRequest) {
        try {
            bulkTransactionRequest = bulkTransactionPersistencePort.addBulkTransaction(bulkTransactionRequest);

            URI uri = new URI("https://sandbox.familybank.co.ke:1044/api/v1/Transaction");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            headers.add("Authorization", "Bearer " + tokenService.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);



            HttpEntity<BulkTransactionRequest> httpEntity = new HttpEntity<>(bulkTransactionRequest, headers);
            ResponseEntity<BulkTransactionResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
                    httpEntity,
                    BulkTransactionResponse.class);

            BulkTransactionResponse response = responseEntity.getBody();
            bulkTransactionPersistencePort.updateBulkTransaction(response, "A");
            return response;

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
