package org.arispay.data.fbl.dtorequest.masspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailRequest {

    @JsonProperty("batchref")
    private String batchRef;

    @JsonProperty("paymentref")
    private String paymentRef;

    @JsonProperty("paymenttype")
    private String paymentType;

    @JsonProperty("senderaccount")
    private String senderAccount;

    @JsonProperty("senderbank")
    private String senderBank;

    @JsonProperty("senderbankbranch")
    private String senderBankBranch;

    @JsonProperty("senderdetails")
    private String senderDetails;

    @JsonProperty("beneficiaryaccount")
    private String beneficiaryAccount;

    @JsonProperty("beneficiarybank")
    private String beneficiaryBank;

    @JsonProperty("beneficiarybankbranch")
    private String beneficiaryBankBranch;

    @JsonProperty("beneficiarydetails")
    private String beneficiaryDetails;

    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("purpose")
    private String purpose;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("paymentamount")
    private double paymentAmount;
}
