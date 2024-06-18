package org.arispay.mappers;

import org.arispay.data.ClientDto;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = CompanyRepository.class)
public abstract class ClientMapper {

    @Autowired
    private CompanyRepository companyRepository;

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    public abstract ClientDto clientToClientDto(Client client);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    public abstract Client clientDtoToClient(ClientDto clientDto);

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    public abstract List<ClientDto> clientListToClientDtoList(List<Client> clientList);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    public abstract List<Client> ClientDtoListToclientList(List<ClientDto> ClientDtoList);

    @Named("idToCompany")
    public Company idToCompany(long id) {
        return companyRepository.getReferenceById(id);
    }

    @Named("companyToId")
    public static Long companyToId(Company company) {
        return company.getId();
    }
}
