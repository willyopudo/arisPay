package org.arispay.repository;

import org.arispay.data.ISummary;
import org.arispay.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//For fresh migrations, always run alter table and add the tsvector column for full text search
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    /*
        * This method retrieves transaction summaries for a given company.
        * first - `collection_count`: Total number of collection transactions.
        * second - `disbursement_count`: Total number of disbursement transactions.
        * third - `total_collection`: Total amount collected.
        * fourth - `total_disbursement`: Total amount disbursed.
     */
    @Query(nativeQuery = true, value="select SUM(CASE WHEN t.cr_dr_ind = 'C' then 1 else 0 END) AS first, SUM(CASE WHEN t.cr_dr_ind = 'D' then 1 else 0 END) AS second, SUM(CASE WHEN t.cr_dr_ind = 'C' then t.tran_amount else 0 END) AS third, SUM(CASE WHEN t.cr_dr_ind = 'D' then t.tran_amount else 0 END) AS fourth  from transactions t where t.company_id  = :companyId")
    Optional<ISummary> getTransactionSummaries(@Param("companyId") Long companyId);

}
