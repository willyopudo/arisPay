package org.arispay.repository;

import org.arispay.data.ISummary;
import org.arispay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


	User findByEmail(String email);

	User findById(int id);

	User findByUsername(String username);

	Optional<User> findByToken(String token);

	/**
	 * This method retrieves user summaries for a given company.
	 * first - `total`: Total number of users.
	 * second - `active`: Total number of active users (record_status = 0).
	 * third - `pending`: Total number of pending users (record_status = 2).
	 * fourth - `inactive`: Total number of inactive users (record_status = 1).
	 */
	@Query(nativeQuery = true, value="SELECT COUNT(u.id) AS first , SUM(CASE WHEN u.record_status = 0 then 1 else 0 END) AS second, SUM(CASE WHEN u.record_status = 2 then 1 else 0 END) AS third, SUM(CASE WHEN u.record_status = 1 then 1 else 0 END) AS fourth FROM users u inner join users_companies uc on u.id  = uc.user_id where uc.company_id = :companyId")
	Optional<ISummary> getUserSummaries(@Param("companyId") Long companyId);
}
