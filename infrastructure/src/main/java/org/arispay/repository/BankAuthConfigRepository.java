package org.arispay.repository;

import org.arispay.entity.BankAuthConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAuthConfigRepository extends JpaRepository<BankAuthConfig, Long> {
    Optional<BankAuthConfig> findByBankCodeAndActiveTrue(String bankCode);
    List<BankAuthConfig> findByActiveTrue();
}
