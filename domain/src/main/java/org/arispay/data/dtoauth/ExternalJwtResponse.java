package org.arispay.data.dtoauth;

public record ExternalJwtResponse(String accessToken,
                                  String tokenType,
                                  int expiresIn) {
    // This record is used to encapsulate the data returned from an external JWT request
    // It includes fields for access token, token type, expiration time, and scope
}
