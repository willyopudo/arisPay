package org.arispay.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.UserFilterDto;
import org.arispay.entity.Role;
import org.arispay.entity.User;
import org.arispay.enums.CurrentPlan;
import org.arispay.enums.RecordStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserSpecification {

    private static final Logger logger = LogManager.getLogger(UserSpecification.class);

    public static Specification<User> getSpecification(UserFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getStatus() != null) {
                try{
                    RecordStatus status = RecordStatus.fromString(filterDto.getStatus());
                    predicates.add(criteriaBuilder.equal(root.get("recordStatus"), status));
                } catch (IllegalArgumentException e) {
                    logger.info("Invalid status: {}. Error message: {}", filterDto.getStatus(), e.getMessage());
                }
            }

            if (filterDto.getCurrentPlan() != null) {
                try {
                    CurrentPlan plan = CurrentPlan.fromString(filterDto.getCurrentPlan());
                    predicates.add(criteriaBuilder.equal(root.get("currentPlan"), plan));
                } catch (IllegalArgumentException e) {
                    logger.info("Invalid plan name: {}. Error message: {}", filterDto.getCurrentPlan(), e.getMessage());
                }
            }

            if (filterDto.getRole() != null) {
                Join<Object, Role> roleJoin = root.join("roles");
                predicates.add(criteriaBuilder.equal(roleJoin.get("name"), filterDto.getRole()));
            }
            if (filterDto.getSearch() != null) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + filterDto.getSearch().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + filterDto.getSearch().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + filterDto.getSearch().toLowerCase() + "%")));
//                        criteriaBuilder.like(criteriaBuilder.lower(root.get("currentPlan").as(String.class)), "%" + filterDto.getSearch().toLowerCase() + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

}
