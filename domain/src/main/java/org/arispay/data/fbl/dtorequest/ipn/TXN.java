package org.arispay.data.fbl.dtorequest.ipn;

import lombok.Data;

import java.util.List;

@Data
public class TXN {
    public String TXN_REF ;
    public String TXN_ACC ;
    public List<TXNDETAIL> TXN_DETAIL ;
}
