package org.arispay.repository;

import org.arispay.entity.Company;
import org.arispay.entity.User;
import org.arispay.entity.UserCompany;
import org.arispay.entity.UserCompanyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, UserCompanyId> {
    List<UserCompany> findByUserId(Long userId);
    UserCompany findByUserIdAndCompanyId(Long userId, Long companyId);
}
