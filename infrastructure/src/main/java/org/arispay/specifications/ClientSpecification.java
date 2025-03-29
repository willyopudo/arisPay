package org.arispay.specifications;

import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.GenericFilterDto;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.enums.ClientIdentifierType;
import org.arispay.enums.RecordStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification {

    private static final Logger logger = LogManager.getLogger(ClientSpecification.class);

    public static Specification<Client> buildComplexSpecification(
            Long companyId,
            String clientId,
            GenericFilterDto filterDto) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Company ID Specification
            if (companyId != null) {
                Join<Company, Client> companyClient = root.join("company");
                predicates.add(criteriaBuilder.equal(companyClient.get("id"), companyId));
            }

            // Client ID Specification
            if (clientId != null && !clientId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("clientId"), clientId));
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

                // Client Identifier Type Filter
                if (filterDto.getFilters().size() > 1 && filterDto.getFilters().get(1) != null && !filterDto.getFilters().get(1).isEmpty()) {
                    try {
                        ClientIdentifierType identifierType = ClientIdentifierType.valueOf(filterDto.getFilters().get(1));
                        predicates.add(criteriaBuilder.equal(root.get("identifierType"), identifierType));
                    } catch (IllegalArgumentException e) {
                        logger.info("Invalid identifier type: {}. Error message: {}", filterDto.getFilters().get(1), e.getMessage());
                    }
                }
            }

            // Combine all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}