package org.arispay.specifications;

import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class ClientSpecification {

    public static Specification<Client> hasCompanyWithId(Long companyId) {
        return (root, query, criteriaBuilder) -> {
            Join<Company, Client> companyClient = root.join("company");
            return criteriaBuilder.equal(companyClient.get("id"), companyId);
        };
    }

    public static Specification<Client> hasClientId(String clientId) {
        return (root, query, cb) -> cb.equal(root.<String>get("clientId"), clientId);
    }
}