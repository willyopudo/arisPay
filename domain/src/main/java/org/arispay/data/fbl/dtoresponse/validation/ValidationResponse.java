package org.arispay.data.fbl.dtoresponse.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_description")
    private String statusDescription;

    @JsonProperty("date_time")
    private String dateTime;

    private Payload payload;
}
