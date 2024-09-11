package org.arispay.service.fbl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.arispay.data.dtoauth.JwtLoginReq;
import org.arispay.data.dtoauth.JwtLoginResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Service
public class TokenService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public String getAccessToken() {
        String FBL_ACCESS_TOKEN = "FBL_ACCESS_TOKEN";
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
