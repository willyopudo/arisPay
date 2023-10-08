package com.arisweb.repository;

import com.arisweb.model.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

}
