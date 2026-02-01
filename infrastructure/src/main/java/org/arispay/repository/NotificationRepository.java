package org.arispay.repository;

import org.arispay.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.company.id = :companyId ORDER BY n.eventTimestamp DESC")
    List<Notification> findByCompanyIdOrderByEventTimestampDesc(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.company.id = :companyId AND n.isSeen = false")
    long countUnreadByCompanyId(@Param("companyId") Long companyId);

    @Modifying
    @Query("UPDATE Notification n SET n.isSeen = true WHERE n.company.id = :companyId AND n.isSeen = false")
    void markAllAsReadByCompanyId(@Param("companyId") Long companyId);

    @Modifying
    @Query("UPDATE Notification n SET n.isSeen = true WHERE n.id = :id")
    void markAsRead(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Notification n SET n.isSeen = false WHERE n.id = :id")
    void markAsUnread(@Param("id") Long id);
}
