package org.arispay.data.fbl.dtorequest.ipn;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class IPN {
    public String DATE;
    public String TYPE ;
    public String ORIGIN ;
    public List<TXN> TXN ;
}
