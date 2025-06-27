package org.arispay.repository;

import org.arispay.data.ISummary;
import org.arispay.entity.CompanyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, Long>, JpaSpecificationExecutor<CompanyAccount> {
    Optional<CompanyAccount> findByAccountNumber(String accountNumber);

    /**
     * This method retrieves account summaries for a given company.
     * first - `first`: Total number of accounts.
     * second - `second`: Total number of active accounts (record_status = 0).
     * third - `third`: Total number of closed accounts (record_status = 2).
     * fourth - `fourth`: Total number of inactive accounts (record_status = 1).
     */
    @Query(nativeQuery = true, value="SELECT  COUNT(ca.id) AS first , SUM(CASE WHEN ca.record_status = 0 then 1 else 0 END) AS second, SUM(CASE WHEN ca.record_status = 2 then 1 else 0 END) AS third, SUM(CASE WHEN ca.record_status = 1 then 1 else 0 END) AS fourth FROM company_accounts ca where ca.company_id = :companyId")
    Optional<ISummary> getCompanyAccountSummaries(@Param("companyId") Long companyId);
}
