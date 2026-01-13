package org.arispay.repository;

import org.arispay.entity.Transaction;
import org.arispay.repository.projections.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Transaction, Long> {

    @Query(nativeQuery = true, value = """
        WITH current_month AS (
            SELECT
                CAST(COUNT(CASE WHEN cr_dr_ind = 'C' THEN 1 END) AS BIGINT) as current_collection_count,
                CAST(COUNT(CASE WHEN cr_dr_ind = 'D' THEN 1 END) AS BIGINT) as current_disbursement_count,
                COALESCE(SUM(CASE WHEN cr_dr_ind = 'C' THEN tran_amount ELSE 0 END), 0) as current_collection_amount,
                COALESCE(SUM(CASE WHEN cr_dr_ind = 'D' THEN tran_amount ELSE 0 END), 0) as current_disbursement_amount
            FROM transactions
            WHERE company_id = :companyId
                AND DATE_TRUNC('month', trans_date) = DATE_TRUNC('month', CURRENT_DATE)
        ),
        previous_month AS (
            SELECT
                CAST(COUNT(CASE WHEN cr_dr_ind = 'C' THEN 1 END) AS BIGINT) as previous_collection_count,
                CAST(COUNT(CASE WHEN cr_dr_ind = 'D' THEN 1 END) AS BIGINT) as previous_disbursement_count,
                COALESCE(SUM(CASE WHEN cr_dr_ind = 'C' THEN tran_amount ELSE 0 END), 0) as previous_collection_amount,
                COALESCE(SUM(CASE WHEN cr_dr_ind = 'D' THEN tran_amount ELSE 0 END), 0) as previous_disbursement_amount
            FROM transactions
            WHERE company_id = :companyId
                AND DATE_TRUNC('month', trans_date) = DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
        )
        SELECT
            cm.current_collection_count as currentCollectionCount,
            pm.previous_collection_count as previousCollectionCount,
            cm.current_disbursement_count as currentDisbursementCount,
            pm.previous_disbursement_count as previousDisbursementCount,
            cm.current_collection_amount as currentCollectionAmount,
            pm.previous_collection_amount as previousCollectionAmount,
            cm.current_disbursement_amount as currentDisbursementAmount,
            pm.previous_disbursement_amount as previousDisbursementAmount
        FROM current_month cm, previous_month pm
    """)
    TransactionMetricsProjection getTransactionMetrics(@Param("companyId") Long companyId);

    @Query(nativeQuery = true, value = """
        SELECT
            TO_CHAR(DATE_TRUNC('month', trans_date), 'Mon') as month,
            payment_mode as paymentMode,
            COUNT(*) as transactionCount
        FROM transactions
        WHERE company_id = :companyId
            AND trans_date >= CURRENT_DATE - (CAST(:months AS INTEGER) * INTERVAL '1 month')
        GROUP BY DATE_TRUNC('month', trans_date), payment_mode, TO_CHAR(DATE_TRUNC('month', trans_date), 'Mon')
        ORDER BY DATE_TRUNC('month', trans_date)
    """)
    List<EarningReportProjection> getEarningReports(@Param("companyId") Long companyId, @Param("months") int months);

    @Query(nativeQuery = true, value = """
        SELECT
            t.payment_mode as paymentMode,
            CASE WHEN t.cr_dr_ind = 'C' THEN 'Credit' WHEN t.cr_dr_ind = 'D' THEN 'Debit' ELSE 'Unknown' END as crDrIndicator,
            t.trans_date as transDate,
            b.bank_code || ' ' || b.bank_name as bankName,
            t.tran_amount as tranAmount,
            c.client_name as clientName
        FROM transactions t
        INNER JOIN company_accounts ca ON t.company_account_id = ca.id
        INNER JOIN bank b ON ca.bank_id = b.id
        LEFT JOIN client c ON t.client_id = c.id
        WHERE t.company_id = :companyId
        ORDER BY t.trans_date DESC
        LIMIT :limit
    """)
    List<LatestTransactionProjection> getLatestTransactions(@Param("companyId") Long companyId, @Param("limit") int limit);

    @Query(nativeQuery = true, value = """
        WITH current_month AS (
            SELECT
                t.client_id,
                c.client_name,
                SUM(t.tran_amount) as current_amount
            FROM transactions t
            INNER JOIN client c ON t.client_id = c.id
            WHERE t.company_id = :companyId
                AND t.client_id IS NOT NULL
                AND t.cr_dr_ind = 'C'
                AND DATE_TRUNC('month', t.trans_date) = DATE_TRUNC('month', CURRENT_DATE)
            GROUP BY t.client_id, c.client_name
        ),
        two_months_ago AS (
            SELECT
                t.client_id,
                SUM(t.tran_amount) as previous_amount
            FROM transactions t
            WHERE t.company_id = :companyId
                AND t.client_id IS NOT NULL
                AND t.cr_dr_ind = 'C'
                AND DATE_TRUNC('month', t.trans_date) = DATE_TRUNC('month', CURRENT_DATE - INTERVAL '2 months')
            GROUP BY t.client_id
        )
        SELECT
            COALESCE(cm.client_id, tm.client_id) as clientId,
            COALESCE(cm.client_name, 'Unknown') as clientName,
            COALESCE(cm.current_amount, 0) as currentAmount,
            COALESCE(tm.previous_amount, 0) as previousAmount
        FROM current_month cm
        FULL OUTER JOIN two_months_ago tm ON cm.client_id = tm.client_id
        WHERE COALESCE(tm.previous_amount, 0) > 0
        ORDER BY
            (COALESCE(cm.current_amount, 0) - COALESCE(tm.previous_amount, 0)) / NULLIF(tm.previous_amount, 0) DESC
        LIMIT :limit
    """)
    List<TopClientProjection> getTopClients(@Param("companyId") Long companyId, @Param("limit") int limit);
}
