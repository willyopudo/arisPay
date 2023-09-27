package com.arisweb.repository;

import com.arisweb.model.CompanyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompanyAccountRepository extends JpaRepository<CompanyAccount, Long> {
}
