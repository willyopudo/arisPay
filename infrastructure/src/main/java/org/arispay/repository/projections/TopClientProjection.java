package org.arispay.repository.projections;

public interface TopClientProjection {
    Long getClientId();
    String getClientName();
    Double getCurrentAmount();
    Double getPreviousAmount();
}
