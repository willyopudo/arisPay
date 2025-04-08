package org.arispay.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.CompanyAccountDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.entity.CompanyAccount;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CompanyAccountSpecification {

    private static final Logger logger = LogManager.getLogger(CompanyAccountSpecification.class);

    public static Specification<CompanyAccount> buildComplexSpecification(
            Long companyId,
            GenericFilterDto filterDto) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Company ID Specification
            if (companyId != null) {
                Join<Company, CompanyAccount> companyAccount = root.join("company");
                predicates.add(criteriaBuilder.equal(companyAccount.get("id"), companyId));
            }

            // Combine all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
