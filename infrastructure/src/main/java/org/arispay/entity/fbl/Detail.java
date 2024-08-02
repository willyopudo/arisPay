package org.arispay.entity.fbl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Detail {
    @JsonProperty("batchref")
    private String batchRef;

    @JsonProperty("paymentref")
    private String paymentRef;

    @JsonProperty("externalref")
    private String externalRef;

    @JsonProperty("cbsref")
    private String cbsRef;

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

    @JsonProperty("xrate")
    private String xRate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("statusdescription")
    private double statusDescription;
}
