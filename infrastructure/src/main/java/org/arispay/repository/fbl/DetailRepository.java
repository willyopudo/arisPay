package org.arispay.repository.fbl;

import org.arispay.entity.fbl.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    @Modifying
    @Query("UPDATE Detail d SET d.status = :status, d.statusDescription = :statusDescription, d.externalRef = :externalRef, d.cbsRef = :cbsRef, d.xRate = :xRate WHERE d.id = :id")
    void updateStatus(@Param("id") Long id,
                      @Param("status") String status,
                      @Param("statusDescription") String statusDescription,
                      @Param("externalRef") String externalRef,
                      @Param("cbsRef") String cbsRef,
                      @Param("xRate") String xRate);

}
