package org.arispay.repository;

import org.arispay.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    //@Query(value = "From Client Where client_Id = ?1", nativeQuery = true)
    Optional<Client> findByClientId(String clientId);
}
