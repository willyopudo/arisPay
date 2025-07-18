package org.arispay.data.fbl.dtoresponse.tsq;

import java.time.LocalDateTime;

public record FblTsqResponse(String transRef,

                             Double amount,

                             String accountNumber,

                             String payerName,

                             String payerPhone,

                             String paymentMode,

                             String narration,

                             String channel,

                             String transDate,

                             String transType) {
}
