package org.arispay.repository.projections;

public interface EarningReportProjection {
    String getMonth();
    String getPaymentMode();
    Long getTransactionCount();
}
