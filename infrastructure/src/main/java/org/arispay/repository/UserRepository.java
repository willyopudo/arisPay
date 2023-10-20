package org.arispay.repository;

import org.arispay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findById(int id);

	User findByUsername(String username);
}
