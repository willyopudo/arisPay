package org.arispay.mappers;

import java.util.List;

import org.arispay.data.TransactionDto;
import org.arispay.entity.Client;
import org.arispay.entity.TransactionRejected;
import org.arispay.repository.ClientRepository;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionRejectedMapper {
    static ClientRepository getClientRepository() {
        return null;
    }

    @Mapping(source = "client", target = "client", qualifiedByName = "clientToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    TransactionDto transactionRejectedToTransactionDto(TransactionRejected transaction);

    @Mapping(source = "client", target = "client", qualifiedByName = "idToClient")
    TransactionRejected transactionDtoToTransactionRejected(TransactionDto transactionDto);

    @Mapping(source = "client", target = "client", qualifiedByName = "clientToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    List<TransactionDto> transactionRejectedListToTransactionDtoList(List<TransactionRejected> transactionRejecteds);

    @Mapping(source = "client", target = "client", qualifiedByName = "idToClient")
    List<TransactionRejected> transactionDtoListToTransactionRejectedList(List<TransactionDto> transactionDtos);

    @Named("idToClient")
    public static Client idToClient(long id) {
        return getClientRepository().getReferenceById(id);
    }

    @Named("clientToId")
    public static Long clientToId(Client client) {
        return client.getId();
    }

    @Named("tranIdToArisTranRef")
    public static String tranIdToArisTranRef(long id) {
        return "ARIS" + String.format("%0" + 9 + "d", id);
    }
}
