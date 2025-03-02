package org.arispay.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.arispay.data.UserFilterDto;
import org.arispay.entity.Role;
import org.arispay.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserSpecification {

    public static Specification<User> getSpecification(UserFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isEnabled"), Objects.equals(filterDto.getStatus(), "active") ? 1:0));
            }

            if (filterDto.getCurrentPlan() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currentPlan"),filterDto.getCurrentPlan()));
            }

            if (filterDto.getRole() != null) {
                Join<Object, Role> roleJoin = root.join("roles");
                predicates.add(criteriaBuilder.equal(roleJoin.get("name"), filterDto.getRole()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
