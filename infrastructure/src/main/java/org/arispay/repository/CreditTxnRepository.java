package org.arispay.repository;

import org.arispay.entity.CreditTxn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditTxnRepository extends JpaRepository<CreditTxn, Long> {
}
