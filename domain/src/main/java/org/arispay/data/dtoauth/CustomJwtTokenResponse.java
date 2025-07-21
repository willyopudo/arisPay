package org.arispay.data.dtoauth;

import lombok.Data;

@Data
public class CustomJwtTokenResponse {
    private String token;
    private Long expiresIn;
    private String type;
}
