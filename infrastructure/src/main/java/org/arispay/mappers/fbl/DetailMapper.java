package org.arispay.mappers.fbl;

import org.arispay.data.fbl.dtorequest.masspayments.DetailRequest;
import org.arispay.entity.fbl.Detail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetailMapper {
    DetailRequest detailToDetailRequest(Detail detail);
    Detail detailRequestToDetail(DetailRequest detailRequest);
    List<DetailRequest> detailListToDetailRequestList(List<Detail> details);
    List<Detail> detailRequestListToDetailList(List<DetailRequest> detailRequests);
}
