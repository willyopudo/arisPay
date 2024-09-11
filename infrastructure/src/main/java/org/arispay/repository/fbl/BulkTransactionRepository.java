package org.arispay.repository.fbl;

import org.arispay.entity.fbl.BulkTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface BulkTransactionRepository extends JpaRepository<BulkTransaction, Long> {
    @Modifying
    @Query("UPDATE BulkTransaction b SET b.status = :status, b.statusDescription = :statusDescription, b.cbsRef = :cbsRef, b.processFlg = :processFlg, b.processTime= :processTime, b.noOfTries = :noOfTries WHERE b.id = :id")
    void updateStatus(@Param("id") Long id,
                      @Param("status") String status,
                      @Param("statusDescription") String statusDescription,
                      @Param("cbsRef") String cbsRef,
                      @Param("processFlg") String processFlg,
                      @Param("processTime") LocalDateTime processTime,
                      @Param("noOfTries") int noOfTries);

    @Query("SELECT b.batchRef " +
            "FROM BulkTransaction b WHERE (CAST(TIME_TO_SEC(TIMEDIFF(b.processTime, :nowTime)) AS DOUBLE) / 60) > :timeInterval " +
            "AND b.processFlg = 'A'")
    List<String> findPendingTransactions(@Param("nowTime") LocalDateTime now,
                                         @Param("timeInterval") int timeInterval);

    @Query("SELECT b " +
            "FROM BulkTransaction b WHERE b.processFlg = 'P' AND (b.postingFlg IS NULL OR b.postingFlg = 'X') AND b.postingTryCount < 3 ")
    List<BulkTransaction> findUnpostedTransactions();

    @Modifying
    @Query("UPDATE BulkTransaction b SET  b.processFlg = :processFlg WHERE b.id = :id")
    void markProcessingStage(@Param("id") Long id, @Param("processFlg") String processFlg);

    @Modifying
    @Query("UPDATE BulkTransaction b SET  b.postingFlg = :postingFlg WHERE b.id = :id")
    void markPostingStage(@Param("id") Long id, @Param("postingFlg") String postingFlg);

    @Modifying
    @Query("UPDATE BulkTransaction b SET  b.postingFlg = :postingFlg, b.postingTime = :postingTime, b.postingTryCount = :postingTryCount WHERE b.id = :id")
    void markPostingStageFinal(@Param("id") Long id,
                                 @Param("postingFlg") String postingFlg,
                                 @Param("postingTime") LocalDateTime postingTime,
                                 @Param("postingTryCount") int postingTryCount);


}
