package org.arispay.repository.fbl;

import org.arispay.entity.fbl.Detail;

import java.util.List;

public interface DetailRepositoryCustom {
    void batchDetailUpdate(List<Detail> details);
}
