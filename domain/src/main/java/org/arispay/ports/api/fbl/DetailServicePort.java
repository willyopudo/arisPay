package org.arispay.ports.api.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.DetailRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.DetailResponse;

import java.util.List;

public interface DetailServicePort {
    DetailRequest addDetail(DetailRequest detailRequest);

    void deleteDetailById(Long id);

    DetailResponse updateDetail(DetailResponse detailResponse);

    List<DetailResponse> getDetails();
}
