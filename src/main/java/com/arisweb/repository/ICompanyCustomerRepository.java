package com.arisweb.repository;

import com.arisweb.model.CompanyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompanyCustomerRepository extends JpaRepository<CompanyCustomer, Long> {
}
