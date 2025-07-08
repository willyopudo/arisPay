package org.arispay.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.GenericFilterDto;
import org.arispay.entity.Transaction;
import org.arispay.entity.TransactionRejected;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QueryTransactionRejectedRepository {

    @PersistenceContext
    private EntityManager em;
    private static final Logger logger = LogManager.getLogger(QueryTransactionRejectedRepository.class);
    public Page<TransactionRejected> searchWithFullText(Long companyId, Pageable pageable, GenericFilterDto filters) {
        String
               bankWhere = "",
               companyAccountWhere = "",
               crDrIndWhere = "",
               searchWhere = "",
                companyWhere = "";

        // Get the date range from the filters
        List<LocalDate> dateRange = new ArrayList<>();
        try {
            if (filters.getFilters() != null && filters.getFilters().get(1) != null && !filters.getFilters().get(1).toString().isEmpty()) {
                dateRange = (List<LocalDate>) filters.getFilters().get(1);
            }
            else{
                dateRange.add(LocalDate.now().minusMonths(2)); // Default to one month ago
                dateRange.add(LocalDate.now()); // Default to today
            }
        } catch (Exception e) {
            logger.error(e);
        }


        String dateWhere = "t.trans_date >= :startDate AND t.trans_date <= :endDate";

        StringBuilder baseQuery = new StringBuilder( "FROM transactions_rejected t   ");
        if (filters != null && filters.getFilters() != null) {

            // Bank  Filter
            if (!filters.getFilters().isEmpty() && filters.getFilters().get(0) != null && !filters.getFilters().get(0).toString().isEmpty()) {
//
                bankWhere = " t.bank_code = :bankCode ";
            }

            //Company Account Filter
            if (!filters.getFilters().isEmpty() && filters.getFilters().get(2) != null && !filters.getFilters().get(2).toString().isEmpty()) {
                companyAccountWhere = " t.company_account_id = :accountId ";
            }

            //CR DR Filter
            if (!filters.getFilters().isEmpty() && filters.getFilters().get(3) != null && !filters.getFilters().get(3).toString().isEmpty()) {
                crDrIndWhere = " t.cr_dr_ind = :crDrInd ";
            }

            //Company Filter
            if (companyId != null) {
                companyWhere = "t.company_id = :companyId";
            }

            // Search Text Filter
            if (filters.getSearch() != null && !filters.getSearch().isEmpty())
                searchWhere = "t.search_vector @@ plainto_tsquery('english', :text)";

        }

        baseQuery.append( " WHERE ");
        baseQuery.append(searchWhere).append(searchWhere.isEmpty() ? " " : " AND ")
                .append(bankWhere).append(bankWhere.isEmpty() ? " " : " AND ")
                .append(companyAccountWhere).append(companyAccountWhere.isEmpty() ? " " : " AND ")
                .append(crDrIndWhere).append(crDrIndWhere.isEmpty() ? " " : " AND ")
                .append(companyWhere).append(companyWhere.isEmpty() ? " " : " AND ")
                .append(dateWhere);

        // Fetch paginated results
        String selectQuery = "SELECT t.* " + baseQuery;
        Query emQuery = em.createNativeQuery(selectQuery, TransactionRejected.class);
        if(!searchWhere.isEmpty())
            emQuery.setParameter( "text", filters.getSearch());
        if(!bankWhere.isEmpty())
            emQuery.setParameter("bankCode", filters.getFilters().getFirst());
        if(!companyAccountWhere.isEmpty())
            emQuery.setParameter("accountId", Long.valueOf(filters.getFilters().get(2).toString()));
        if(!crDrIndWhere.isEmpty())
            emQuery.setParameter("crDrInd", filters.getFilters().get(3));
        if(!companyWhere.isEmpty())
            emQuery.setParameter("companyId", companyId);
        emQuery.setParameter("startDate", dateRange.getFirst());
        emQuery.setParameter("endDate", dateRange.getLast().plusDays(1));



        List<TransactionRejected> results =  emQuery.setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Count total matches
        String countQuery = "SELECT COUNT(*) " + baseQuery;

        Query emTotalQuery = em.createNativeQuery(countQuery);
        if(!searchWhere.isEmpty())
            emTotalQuery.setParameter( "text", filters.getSearch());
        if(!bankWhere.isEmpty())
            emTotalQuery.setParameter("bankCode", filters.getFilters().getFirst());
        if(!companyAccountWhere.isEmpty())
            emTotalQuery.setParameter("accountId", Long.valueOf(filters.getFilters().get(2).toString()));
        if(!crDrIndWhere.isEmpty())
            emTotalQuery.setParameter("crDrInd", filters.getFilters().get(3));
        if(!companyWhere.isEmpty())
            emTotalQuery.setParameter("companyId", companyId);
        emTotalQuery.setParameter("startDate", dateRange.getFirst());
        emTotalQuery.setParameter("endDate", dateRange.getLast().plusDays(1));

        Long total = ((Number) emTotalQuery.getSingleResult()).longValue();

        return new PageImpl<>(results, pageable, total);
    }
}
