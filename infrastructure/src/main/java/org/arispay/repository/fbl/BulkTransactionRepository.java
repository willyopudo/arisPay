package org.arispay.repository.fbl;

import org.arispay.entity.fbl.BulkTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulkTransactionRepository extends JpaRepository<BulkTransaction, Long> {
}
