package org.arispay.repository;

import org.arispay.data.IUserSummary;
import org.arispay.data.UserSummary;
import org.arispay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


	User findByEmail(String email);

	User findById(int id);

	User findByUsername(String username);

	Optional<User> findByToken(String token);

	@Query(nativeQuery = true, value="SELECT COUNT(u.id) AS totalUsers , SUM(CASE WHEN u.record_status = 0 then 1 else 0 END) AS activeUsers, SUM(CASE WHEN u.record_status = 2 then 1 else 0 END) AS pendingUsers, SUM(CASE WHEN u.record_status = 1 then 1 else 0 END) AS inactiveUsers FROM users u inner join users_companies uc on u.id  = uc.user_id where uc.company_id = :companyId")
	Optional<IUserSummary> getUserSummaries(@Param("companyId") Long companyId);
}
