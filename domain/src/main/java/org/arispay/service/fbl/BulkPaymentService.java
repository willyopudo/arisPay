package org.arispay.service.fbl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.arispay.data.dtoauth.JwtLoginReq;
import org.arispay.data.dtoauth.JwtLoginResp;
import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class BulkPaymentService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private final String FBL_ACCESS_TOKEN = "FBL_ACCESS_TOKEN";

    public BulkTransactionResponse initiateBulkTransactionRequest(BulkTransactionRequest bulkTransactionRequest) {
        try {
            URI uri = new URI("https://sandbox.familybank.co.ke:1044/api/v1/Transaction");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            headers.add("Authorization", "Bearer " + getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<BulkTransactionRequest> httpEntity = new HttpEntity<>(bulkTransactionRequest, headers);
            ResponseEntity<BulkTransactionResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST,
                    httpEntity,
                    BulkTransactionResponse.class);

            BulkTransactionResponse response = responseEntity.getBody();

            return response;

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAccessToken() {
        JwtLoginResp tokenBody = (JwtLoginResp) redisTemplate.opsForValue().get(FBL_ACCESS_TOKEN);
        String token = tokenBody == null ? null : tokenBody.getAccessToken();
        if (token == null || isTokenExpired(token)) {
            token = generateNewToken();
            redisTemplate.opsForValue().set(FBL_ACCESS_TOKEN, token);
        }
        return token;
    }

    private boolean isTokenExpired(String token) {
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.decode(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            return expiresAt.before(new Date());
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return true;
        }
    }

    private String generateNewToken() {
        try {
            URI uri = new URI("https://sandbox.familybank.co.ke:1045/connect/token");
            JwtLoginReq tokenRequest = new JwtLoginReq("client@sandbox.familybank.co.ke", "#secret123$",
                    "client_credentials", "OB_BULK_PAY");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<JwtLoginReq> httpEntity = new HttpEntity<>(tokenRequest, headers);
            ResponseEntity<JwtLoginResp> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity,
                    JwtLoginResp.class);

            JwtLoginResp response = responseEntity.getBody();

            return response == null ? null : response.getAccessToken();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
