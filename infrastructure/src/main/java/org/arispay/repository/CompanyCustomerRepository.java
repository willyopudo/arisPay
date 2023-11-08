package org.arispay.repository;

import org.arispay.entity.CompanyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCustomerRepository extends JpaRepository<CompanyCustomer, Long> {
}
