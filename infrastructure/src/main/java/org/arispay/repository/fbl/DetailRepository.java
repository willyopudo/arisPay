package org.arispay.repository.fbl;

import org.arispay.entity.fbl.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailRepository extends JpaRepository<Detail, Long>, DetailRepositoryCustom {

}
