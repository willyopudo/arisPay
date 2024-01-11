package org.arispay.repository;

import org.arispay.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    @Query(value = "From Client Where clientId = ?1", nativeQuery = true)
    Optional<Client> findClientByClientId(String clientId);
}
