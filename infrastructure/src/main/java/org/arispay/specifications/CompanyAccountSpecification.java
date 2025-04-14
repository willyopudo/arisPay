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
import org.arispay.enums.ClientIdentifierType;
import org.arispay.enums.RecordStatus;
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

            // Generic Filter Specifications
            if (filterDto != null && filterDto.getFilters() != null) {
                // Record Status Filter
                if (!filterDto.getFilters().isEmpty() && filterDto.getFilters().get(0) != null && !filterDto.getFilters().get(0).isEmpty()) {
                    try {
                        RecordStatus status = RecordStatus.fromString(filterDto.getFilters().get(0));
                        predicates.add(criteriaBuilder.equal(root.get("recordStatus"), status));
                    } catch (IllegalArgumentException e) {
                        logger.info("Invalid status: {}. Error message: {}", filterDto.getFilters().get(0), e.getMessage());
                    }
                }

                if (filterDto.getSearch() != null) {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("accountName")), "%" + filterDto.getSearch().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("accountNumber")), "%" + filterDto.getSearch().toLowerCase() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("bank").get("bankName")), "%" + filterDto.getSearch().toLowerCase() + "%")));
//                        criteriaBuilder.like(criteriaBuilder.lower(root.get("currentPlan").as(String.class)), "%" + filterDto.getSearch().toLowerCase() + "%")));
                }
            }

            // Combine all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
