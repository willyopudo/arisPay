package org.arispay.specifications;

import jakarta.persistence.criteria.Join;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {
    public static Specification<Transaction> hasCompanyWithId(Long companyId) {
        return (root, query, criteriaBuilder) -> {
            Join<Company, Transaction> companyTransaction = root.join("company");
            return criteriaBuilder.equal(companyTransaction.get("id"), companyId);
        };
    }

    public static Specification<Transaction> hasClientWithId(Long clientId) {
        return (root, query, criteriaBuilder) -> {
            Join<Client, Transaction> clientTransaction = root.join("client");
            return criteriaBuilder.equal(clientTransaction.get("id"), clientId);
        };
    }
}
