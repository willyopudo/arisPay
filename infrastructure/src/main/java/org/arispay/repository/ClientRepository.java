package org.arispay.repository;

import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    // @Query(value = "From Client Where client_Id = ?1", nativeQuery = true)
    Optional<Client> findByClientId(String clientId);

    Page<Client> findByCompany(Company company, Pageable pageable);
}
