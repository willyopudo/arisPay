package org.arispay.mappers;

import java.util.List;

import org.arispay.data.TransactionDto;
import org.arispay.entity.Client;
import org.arispay.entity.TransactionRejected;
import org.arispay.repository.ClientRepository;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ClientRepository.class)
public abstract class TransactionRejectedMapper {

    @Autowired
    private ClientRepository clientRepository;

    @Mapping(source = "client", target = "client", qualifiedByName = "clientToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    public abstract TransactionDto transactionRejectedToTransactionDto(TransactionRejected transaction);

    @Mapping(source = "client", target = "client", qualifiedByName = "idToClient")
    public abstract TransactionRejected transactionDtoToTransactionRejected(TransactionDto transactionDto);

    @Mapping(source = "client", target = "client", qualifiedByName = "clientToId")
    @Mapping(source = "id", target = "arisTranRef", qualifiedByName = "tranIdToArisTranRef")
    public abstract List<TransactionDto> transactionRejectedListToTransactionDtoList(
            List<TransactionRejected> transactionRejecteds);

    @Mapping(source = "client", target = "client", qualifiedByName = "idToClient")
    public abstract List<TransactionRejected> transactionDtoListToTransactionRejectedList(
            List<TransactionDto> transactionDtos);

    @Named("idToClient")
    public Client idToClient(String id) {
        return clientRepository.findClientByClientId(id).orElse(null);
    }

    @Named("clientToId")
    public static String clientToId(Client client) {
        return client.getClientId();
    }

    @Named("tranIdToArisTranRef")
    public static String tranIdToArisTranRef(long id) {
        return "ARISF" + String.format("%0" + 9 + "d", id);
    }
}
