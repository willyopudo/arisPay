package org.arispay.ports.spi;

import org.arispay.data.BankEndpointDto;

import java.util.List;

public interface BankEndpointPersistencePort {
    String getEndpointUrl(String bankCode, String endpointName);
    List<BankEndpointDto> getEndpoints(String bankCode);
}
