package com.arisweb.repository;

import com.arisweb.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompanyRepository extends JpaRepository<Company, Long> {
}
