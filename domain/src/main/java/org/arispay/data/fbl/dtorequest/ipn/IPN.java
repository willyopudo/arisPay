package org.arispay.data.fbl.dtorequest.ipn;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class IPN {
    public Date DATE;
    public String TYPE ;
    public String ORIGIN ;
    public List<TXN> TXN ;
}
