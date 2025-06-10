package org.arispay.data.fbl.dtorequest.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationPayload {
    private String identifier;
    @JsonProperty("identifier_type")
    private String identifierType;
    @JsonProperty("collection_account")
    private String collectionAccount;
}
