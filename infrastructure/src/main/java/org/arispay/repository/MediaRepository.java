package org.arispay.repository;

import org.arispay.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	Media findByName(final String name);

	boolean existsByNameIgnoreCase(String name);
}
