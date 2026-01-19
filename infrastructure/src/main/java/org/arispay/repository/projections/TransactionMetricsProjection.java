package org.arispay.repository.projections;

public interface TransactionMetricsProjection {
    Long getCurrentCollectionCount();
    Long getPreviousCollectionCount();
    Long getCurrentDisbursementCount();
    Long getPreviousDisbursementCount();
    Double getCurrentCollectionAmount();
    Double getPreviousCollectionAmount();
    Double getCurrentDisbursementAmount();
    Double getPreviousDisbursementAmount();
}
