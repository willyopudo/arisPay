package org.arispay.service;

import org.arispay.data.SelectDto;
import org.arispay.ports.api.BankServicePort;
import org.arispay.ports.spi.BankPersistencePort;

import java.util.List;

public class BankServiceImpl implements BankServicePort {
    private final BankPersistencePort bankPersistencePort;

    public BankServiceImpl(BankPersistencePort bankPersistencePort) {
        this.bankPersistencePort = bankPersistencePort;
    }

    @Override
    public List<SelectDto> getBanks() {
        return bankPersistencePort.getBanks();
    }
}
