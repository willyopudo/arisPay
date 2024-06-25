package org.arispay.repository;

import org.arispay.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    // @Query(value = "From Client Where client_Id = ?1", nativeQuery = true)
    Optional<Client> findByClientId(String clientId);
}
