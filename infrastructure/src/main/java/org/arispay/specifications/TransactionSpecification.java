package org.arispay.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.GenericFilterDto;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.entity.Transaction;
import org.arispay.enums.ClientIdentifierType;
import org.arispay.enums.RecordStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    private static final Logger logger = LogManager.getLogger(TransactionSpecification.class);

    public static Specification<Transaction> buildComplexSpecification(
            Long companyId,
            String clientId,
            GenericFilterDto filterDto) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Company ID Specification
            if (companyId != null) {
                Join<Company, Transaction> companyTransaction = root.join("company");
                predicates.add(criteriaBuilder.equal(companyTransaction.get("id"), companyId));
            }

            // Client ID Specification
            if (clientId != null && !clientId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("client").get("clientId"), clientId));
            }

            //Generic Filter Specifications
            if (filterDto != null && filterDto.getFilters() != null) {
                // Bank  Filter
                if (!filterDto.getFilters().isEmpty() && filterDto.getFilters().get(0) != null && !filterDto.getFilters().get(0).toString().isEmpty()) {
                    try {
                        predicates.add(criteriaBuilder.equal(root.get("companyAccount").get("bank").get("bankCode"), filterDto.getFilters().getFirst()));
                    } catch (IllegalArgumentException e) {
                        logger.info("Invalid bank: {}. Error message: {}", filterDto.getFilters().getFirst(), e.getMessage());
                    }
                }
                List<LocalDate> dateRange = (List<LocalDate>) filterDto.getFilters().get(2);
                if (filterDto.getFilters().get(2) != null && !dateRange.isEmpty()) {

                    try {

                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transDate"), dateRange.getFirst()));
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transDate"), dateRange.getLast().plusDays(1)));
                    } catch (Exception e) {
                        logger.info("Invalid dateRange: {}. Error message: {}", filterDto.getFilters().get(2).toString(), e.getMessage());
                    }
                }

                if (!filterDto.getFilters().isEmpty() && filterDto.getFilters().get(3) != null && !filterDto.getFilters().get(3).toString().isEmpty()) {
                    try {
                        predicates.add(criteriaBuilder.equal(root.get("companyAccount").get("id"), filterDto.getFilters().get(3)));
                    } catch (IllegalArgumentException e) {
                        logger.info("Invalid Company Account: {}. Error message: {}", filterDto.getFilters().get(3), e.getMessage());
                    }
                }

                if (!filterDto.getFilters().isEmpty() && filterDto.getFilters().get(4) != null && !filterDto.getFilters().get(4).toString().isEmpty()) {
                    try {
                        predicates.add(criteriaBuilder.equal(root.get("crDrInd"), filterDto.getFilters().get(4)));
                    } catch (IllegalArgumentException e) {
                        logger.info("Invalid CR DR Indicator: {}. Error message: {}", filterDto.getFilters().get(4), e.getMessage());
                    }
                }

                // Client Identifier Type Filter
//                if (filterDto.getFilters().size() > 1 && filterDto.getFilters().get(1) != null && !filterDto.getFilters().get(1).isEmpty()) {
//                    try {
//                        ClientIdentifierType identifierType = ClientIdentifierType.valueOf(filterDto.getFilters().get(1));
//                        predicates.add(criteriaBuilder.equal(root.get("identifierType"), identifierType));
//                    } catch (IllegalArgumentException e) {
//                        logger.info("Invalid identifier type: {}. Error message: {}", filterDto.getFilters().get(1), e.getMessage());
//                    }
//                }

            }

            // Combine all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
