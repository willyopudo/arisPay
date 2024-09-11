package org.arispay.repository;

import org.arispay.entity.TransactionRejected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRejectedRepository extends JpaRepository<TransactionRejected, Long>, JpaSpecificationExecutor<TransactionRejected> {
}
