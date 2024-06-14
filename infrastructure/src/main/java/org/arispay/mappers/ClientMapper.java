package org.arispay.mappers;

import org.arispay.data.ClientDto;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    static CompanyRepository getCompanyRepository() {
        return null;
    }

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    ClientDto clientToClientDto(Client client);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    Client clientDtoToClient(ClientDto clientDto);

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    List<ClientDto> clientListToClientDtoList(List<Client> clientList);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    List<Client> ClientDtoListToclientList(List<ClientDto> ClientDtoList);

    @Named("idToCompany")
    public static Company idToCompany(long id) {
        return getCompanyRepository().getReferenceById(id);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }
}
