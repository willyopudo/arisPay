package org.arispay.data.fbl.dtorequest.masspayments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BulkTransactionRequest {
    @JsonProperty("batchref")
    private String batchRef;

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

    @JsonProperty("dtl")
    private List<DetailRequest> dtl;
}
