package org.arispay.data.fbl.dtoresponse.masspayments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BulkTransactionResponse {
    @JsonProperty("batchref")
    private String batchRef;

    @JsonProperty("cbsref")
    private String cbsRef;

    @JsonProperty("accountdr")
    private String accountDr;

    @JsonProperty("narration")
    private String narration;

    @JsonProperty("valuedate")
    private String valueDate;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("totalamount")
    private double totalAmount;

    @JsonProperty("status")
    private String status;

    @JsonProperty("statusdescription")
    private double statusDescription;

    @JsonProperty("dtl")
    private List<DetailResponse> dtl;
}
