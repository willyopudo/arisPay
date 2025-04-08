package org.arispay.repository;

import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    // @Query(value = "From Client Where client_Id = ?1", nativeQuery = true)
    Optional<Client> findByClientId(String clientId);

    Page<Client> findByCompany(Company company, Pageable pageable);

    @Procedure(name = "insert_client",
               outputParameterName = "p_client_id")
    String insertClient(
            @Param("p_client_name") String clientName,
            @Param("p_client_id_in") String clientId,
            @Param("p_client_email") String clientEmail,
            @Param("p_identifier_type") String identifierType,
            @Param("p_client_phone") String clientPhone,
            @Param("p_status") byte status,
            @Param("p_created_by") String createdBy,
            @Param("p_company_id") Integer companyId
    );



}
