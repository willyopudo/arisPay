package org.arispay.data.dtorequest.confirmation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("payer_name")
    private String payerName;
    @JsonProperty("payer_phone")
    private String payerPhone;
    @JsonProperty("txn_amount")
    private Double txnAmount;
    @JsonProperty("payment_mode")
    private String paymentMode;
    @JsonProperty("txn_reference")
    private String txnReference;
    @JsonProperty("collection_account")
    private String collectionAccount;
    @JsonProperty("txn_narration")
    private String txnNarration;
    @JsonProperty("date_time")
    private String dateTime;




}
