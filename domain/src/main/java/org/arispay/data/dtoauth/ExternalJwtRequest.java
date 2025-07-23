package org.arispay.data.dtoauth;

public record ExternalJwtRequest(String clientId,
                                 String clientSecret,
                                 String grantType,
                                 String scope) {
    // This record is used to encapsulate the data required for an external JWT request
    // It includes fields for client ID, client secret, grant type, scope, and audience
}
