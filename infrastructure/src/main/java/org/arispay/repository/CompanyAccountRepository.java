package org.arispay.repository;

import org.arispay.data.ISummary;
import org.arispay.entity.Client;
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

    @Query(nativeQuery = true, value="SELECT  COUNT(ca.id) AS total , SUM(CASE WHEN ca.record_status = 0 then 1 else 0 END) AS active, SUM(CASE WHEN ca.record_status = 2 then 1 else 0 END) AS pending, SUM(CASE WHEN ca.record_status = 1 then 1 else 0 END) AS inactive FROM public.company_accounts ca where ca.company_id = :companyId")
    Optional<ISummary> getCompanyAccountSummaries(@Param("companyId") Long companyId);
}
