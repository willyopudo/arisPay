package org.arispay.mappers;

import org.arispay.data.ClientDto;
import org.arispay.data.UserDto;
import org.arispay.entity.Client;
import org.arispay.entity.Company;
import org.arispay.entity.User;
import org.arispay.repository.CompanyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring", uses = CompanyRepository.class)
public abstract class ClientMapper {

    @Autowired
    private CompanyRepository companyRepository;
    //dummy comment
    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    @Mapping(source = "company", target = "companyName", qualifiedByName = "companyToName")
    public abstract ClientDto clientToClientDto(Client client);

    @Mapping(source = "company", target = "company", qualifiedByName = "idToCompany")
    public abstract Client clientDtoToClient(ClientDto clientDto);

    @Mapping(source = "company", target = "company", qualifiedByName = "companyToId")
    @Mapping(source = "company", target = "companyName", qualifiedByName = "companyToName")
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
    @Named("companyToName")
    public static String companyToName(Company company) {
        return company.getName();
    }

    public Page<ClientDto> clientsPagetoClientsDtoPage(Page<Client> clientsPage) {
        List<ClientDto> dtoList = clientListToClientDtoList(clientsPage.getContent());  // Convert list
        return new PageImpl<>(dtoList, clientsPage.getPageable(), clientsPage.getTotalElements());
    }
}
