package org.arispay.repository.fbl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.arispay.entity.fbl.Detail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DetailRepositoryImpl implements DetailRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Transactional
    public void batchDetailUpdate(List<Detail> details) {
        int batchSize = 100;

        for(int i= 0; i < details.size(); i += batchSize) {
            int end = Math.min(i + batchSize, details.size());
            List<Detail> batch = details.subList(i, end);

            // Construct JPQL query
            String jpql = "UPDATE Detail d SET " +
                    "d.status = CASE d.id " +
                    batch.stream()
                            .map(d -> "WHEN :id" + d.getId() + " THEN :status" + d.getId())
                            .collect(Collectors.joining(" ")) +
                    " END, " +
                    "d.statusDescription = CASE d.id " +
                    batch.stream()
                            .map(d -> "WHEN :id" + d.getId() + " THEN :statusDescription" + d.getId())
                            .collect(Collectors.joining(" ")) +
                    " END, " +
                    "d.externalRef = CASE d.id " +
                    batch.stream()
                            .map(d -> "WHEN :id" + d.getId() + " THEN :externalRef" + d.getId())
                            .collect(Collectors.joining(" ")) +
                    " END, " +
                    "d.cbsRef = CASE d.id " +
                    batch.stream()
                            .map(d -> "WHEN :id" + d.getId() + " THEN :cbsRef" + d.getId())
                            .collect(Collectors.joining(" ")) +
                    " END, " +
                    "d.xRate = CASE d.id " +
                    batch.stream()
                            .map(d -> "WHEN :id" + d.getId() + " THEN :xRate" + d.getId())
                            .collect(Collectors.joining(" ")) +
                    " END " +
                    "WHERE d.id IN :ids";

            var query = entityManager.createQuery(jpql);

            // Set parameters
            batch.forEach(detail -> {
                query.setParameter("id" + detail.getId(), detail.getId());
                query.setParameter("status" + detail.getId(), detail.getStatus());
                query.setParameter("statusDescription" + detail.getId(), detail.getStatusDescription());
                query.setParameter("externalRef" + detail.getId(), detail.getExternalRef());
                query.setParameter("cbsRef" + detail.getId(), detail.getCbsRef());
                query.setParameter("xRate" + detail.getId(), detail.getXRate());
            });
            query.setParameter("ids", batch.stream().map(Detail::getId).collect(Collectors.toList()));

            query.executeUpdate();

            //Flush and clear the persistence context
            entityManager.flush();
            entityManager.clear();

        }
    }
}
