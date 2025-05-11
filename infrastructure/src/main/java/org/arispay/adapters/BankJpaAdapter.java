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
        // Fetch all banks from the repository
        // and map them to SelectDto objects
        // using a stream to transform the list
        // into a list of SelectDto
        // using the bank code and name

        List<Bank> banks = bankRepository.findAll();
        return banks.stream()
                .sorted((b1, b2) -> b1.getBankCode().compareTo(b2.getBankCode()))
                .map(bank -> new SelectDto(bank.getBankCode() + " " + bank.getBankName(), bank.getBankCode()))
                .toList();
    }
}
