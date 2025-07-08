package org.arispay.repository;

import org.arispay.data.ISummary;
import org.arispay.entity.TransactionRejected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRejectedRepository extends JpaRepository<TransactionRejected, Long>, JpaSpecificationExecutor<TransactionRejected> {

    /*
     * This method retrieves transaction summaries for a given company.
     * first - `collection_count`: Total number of collection transactions.
     * second - `disbursement_count`: Total number of disbursement transactions.
     * third - `total_collection`: Total amount collected.
     * fourth - `total_disbursement`: Total amount disbursed.
     */
    @Query(nativeQuery = true, value="select SUM(CASE WHEN t.cr_dr_ind = 'C' then 1 else 0 END) AS first, SUM(CASE WHEN t.cr_dr_ind = 'D' then 1 else 0 END) AS second, SUM(CASE WHEN t.cr_dr_ind = 'C' then t.tran_amount else 0 END) AS third, SUM(CASE WHEN t.cr_dr_ind = 'D' then t.tran_amount else 0 END) AS fourth  from transactions_rejected t where t.company_id  = :companyId")
    Optional<ISummary> getTransactionRejectedSummaries(@Param("companyId") Long companyId);
}
