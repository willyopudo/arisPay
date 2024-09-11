package org.arispay.data.fbl.dtoresponse.masspayments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DetailResponse {
    @JsonProperty("batchref")
    private String batchRef;

    @JsonProperty("paymentref")
    private String paymentRef;

    @JsonProperty("externalref")
    private String externalRef;

    @JsonProperty("cbsref")
    private String cbsRef;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("xrate")
    private String xRate;

    @JsonProperty("paymentamount")
    private double paymentAmount;

    @JsonProperty("status")
    private String status;

    @JsonProperty("statusdescription")
    private double statusDescription;
}
