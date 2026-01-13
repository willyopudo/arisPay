package org.arispay.repository.projections;

import java.time.LocalDateTime;

public interface LatestTransactionProjection {
    String getPaymentMode();
    String getCrDrIndicator();
    LocalDateTime getTransDate();
    String getBankName();
    Double getTranAmount();
    String getClientName();
}
