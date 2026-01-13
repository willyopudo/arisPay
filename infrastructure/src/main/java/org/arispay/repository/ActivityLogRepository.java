package org.arispay.repository;

import org.arispay.entity.ActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Query("SELECT a FROM ActivityLog a WHERE a.company.id = :companyId ORDER BY a.eventTimestamp DESC")
    List<ActivityLog> findTopNByCompanyIdOrderByEventTimestampDesc(@Param("companyId") Long companyId, Pageable pageable);
}
