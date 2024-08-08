package org.arispay.mappers.fbl;

import java.util.List;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.entity.fbl.BulkTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BulkTransactionMapper {
    BulkTransactionRequest bulkTransToBulkTransRequest(BulkTransaction bulkTransaction);

    BulkTransaction bulkTransRequestToBulkTrans(BulkTransactionRequest bulkTransactionRequest);

    List<BulkTransactionRequest> bulkTransListToBulkTransRequestList(List<BulkTransaction> bulkTransactions);

    List<BulkTransaction> bulkTransRequestListToBulkTransListBulkTransRequestList(
            List<BulkTransactionRequest> bulkTransactionRequests);
}
