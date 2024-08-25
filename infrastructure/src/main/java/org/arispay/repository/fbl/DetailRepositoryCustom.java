package org.arispay.repository.fbl;


import org.arispay.entity.fbl.Detail;

import java.util.List;
import java.util.Map;

public interface DetailRepositoryCustom {
    void batchDetailUpdate(List<Detail> details);
}
