package org.arispay.data.dtorequest.Validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private String identifier;
    private String identifier_type;
    private String collection_account;
}
