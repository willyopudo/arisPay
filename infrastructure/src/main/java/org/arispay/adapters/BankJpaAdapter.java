package org.arispay.adapters;

import org.arispay.data.SelectDto;
import org.arispay.entity.Bank;
import org.arispay.ports.spi.BankPersistencePort;
import org.arispay.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankJpaAdapter implements BankPersistencePort {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public List<SelectDto> getBanks() {
        List<Bank> banks = bankRepository.findAll();
        return banks.stream()
                .map(bank -> new SelectDto(bank.getBankName(), bank.getBankCode()))
                .toList();
    }
}
