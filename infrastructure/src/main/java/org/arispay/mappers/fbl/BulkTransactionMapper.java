package org.arispay.mappers.fbl;

import java.util.List;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.entity.fbl.BulkTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BulkTransactionMapper {
    BulkTransactionRequest bulkTransToBulkTransRequest(BulkTransaction bulkTransaction);

    BulkTransaction bulkTransRequestToBulkTrans(BulkTransactionRequest bulkTransactionRequest);

    BulkTransactionResponse bulkTransToBulkTransResponse(BulkTransaction bulkTransaction);

    BulkTransaction bulkTransResponseToBulkTrans(BulkTransactionResponse bulkTransactionResponse);

    List<BulkTransactionRequest> bulkTransListToBulkTransRequestList(List<BulkTransaction> bulkTransactions);

    List<BulkTransaction> bulkTransRequestListToBulkTransListBulkTransList(
            List<BulkTransactionRequest> bulkTransactionRequests);

    List<BulkTransactionResponse> bulkTransListToBulkTransResponseList(List<BulkTransaction> bulkTransactions);

    List<BulkTransaction> bulkTransResponseListToBulkTransListBulkTransList(
            List<BulkTransactionResponse> bulkTransactionResponses);
}
