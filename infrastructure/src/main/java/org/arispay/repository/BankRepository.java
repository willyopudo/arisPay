package org.arispay.repository;

import org.arispay.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    // This interface extends BaseRepository to inherit common CRUD operations
    // and can be used to define custom query methods specific to Bank entity.
    // For example:
    // List<Bank> findByBankName(String bankName);
     Optional<Bank> findByBankCode(String bankCode);
}
