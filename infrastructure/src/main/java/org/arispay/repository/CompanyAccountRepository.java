package org.arispay.repository;

import java.util.Optional;

import org.arispay.entity.CompanyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, Long> {

    Optional<CompanyAccount> findByAccountNumber(String accountNumber);
}
