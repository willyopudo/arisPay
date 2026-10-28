package org.arispay.repository;

import org.arispay.entity.BankEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankEndpointRepository extends JpaRepository<BankEndpoint, Long> {
    Optional<BankEndpoint> findByBankCodeAndEndpointNameAndActiveTrue(String bankCode, String endpointName);
    List<BankEndpoint> findByBankCodeAndActiveTrue(String bankCode);
}
