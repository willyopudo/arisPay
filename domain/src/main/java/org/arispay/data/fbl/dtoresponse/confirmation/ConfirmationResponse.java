package org.arispay.data.fbl.dtoresponse.confirmation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationResponse {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("status_description")
    private String statusDescription;
    @JsonProperty("payment_ref")
    private String paymentRef;
    @JsonProperty("date_time")
    private String dateTime;
}
