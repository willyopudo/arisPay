package org.arispay.mappers.fbl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.arispay.data.fbl.dtorequest.masspayments.BulkTransactionRequest;
import org.arispay.data.fbl.dtoresponse.masspayments.BulkTransactionResponse;
import org.arispay.entity.Bank;
import org.arispay.entity.fbl.BulkTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class BulkTransactionMapper {
    public abstract BulkTransactionRequest bulkTransToBulkTransRequest(BulkTransaction bulkTransaction);

    @Mapping(source = "valueDate", target = "valueDate", qualifiedByName = "stringToValueDate")
    public abstract BulkTransaction bulkTransRequestToBulkTrans(BulkTransactionRequest bulkTransactionRequest);

    public abstract BulkTransactionResponse bulkTransToBulkTransResponse(BulkTransaction bulkTransaction);

    public abstract BulkTransaction bulkTransResponseToBulkTrans(BulkTransactionResponse bulkTransactionResponse);

    public abstract List<BulkTransactionRequest> bulkTransListToBulkTransRequestList(List<BulkTransaction> bulkTransactions);

    public abstract List<BulkTransaction> bulkTransRequestListToBulkTransListBulkTransList(
            List<BulkTransactionRequest> bulkTransactionRequests);

    public abstract List<BulkTransactionResponse> bulkTransListToBulkTransResponseList(List<BulkTransaction> bulkTransactions);

    public abstract List<BulkTransaction> bulkTransResponseListToBulkTransListBulkTransList(
            List<BulkTransactionResponse> bulkTransactionResponses);

    @Named("stringToValueDate")
    public LocalDateTime stringToValueDate(String valueDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(valueDate, formatter);
    }
}
