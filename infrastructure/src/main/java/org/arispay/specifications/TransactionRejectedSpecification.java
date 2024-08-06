package org.arispay.specifications;

import jakarta.persistence.criteria.Join;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.entity.Transaction;
import org.arispay.entity.TransactionRejected;
import org.springframework.data.jpa.domain.Specification;

public class TransactionRejectedSpecification {
    public static Specification<TransactionRejected> hasCompanyWithId(Long companyId) {
        return (root, query, criteriaBuilder) -> {
            Join<Company, TransactionRejected> companyTransaction = root.join("company");
            return criteriaBuilder.equal(companyTransaction.get("id"), companyId);
        };
    }

    public static Specification<TransactionRejected> hasClientWithId(String clientId) {
        return (root, query, criteriaBuilder) -> {
            Join<Client, TransactionRejected> clientTransaction = root.join("client");
            return criteriaBuilder.equal(clientTransaction.get("clientId"), clientId);
        };
    }

    public static Specification<TransactionRejected> hasBankAccount(String accountNo) {
        return (root, query, cb) -> cb.equal(root.<String>get("bankAccount"), accountNo);
    }
}
