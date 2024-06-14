package org.arispay.data.fbl.dtorequest.ipn;

import lombok.Data;

@Data
public class TXNDETAIL {
    public String TXN_CODE ;
    public String TXN_SWIFT ;
    public String TXN_NO ;
    public String TXN_TYPE ;
    public String TXN_AMT_TAG ;
    public String TXN_AMT ;
    public String TXN_DATE ;
    public String TXN_VDATE ;
    public String TXN_NARRATION ;
}
