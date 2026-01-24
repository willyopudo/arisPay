package org.arispay.ports.spi;

import org.arispay.data.SelectDto;

import java.util.List;

public interface BankPersistencePort {
    List<SelectDto> getBanks();
}
