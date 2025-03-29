package org.arispay.adapters;

import org.arispay.data.ClientDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.entity.Client;
import org.arispay.enums.RecordStatus;
import org.arispay.mappers.ClientMapper;
import org.arispay.ports.spi.ClientPersistencePort;
import org.arispay.repository.ClientRepository;
import org.arispay.specifications.ClientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientJpaAdapter implements ClientPersistencePort {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public ClientDto addClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        client.setRecordStatus(RecordStatus.ACTIVE);
        if(client.getClientId() == null || client.getClientId().isEmpty()){
            client.setClientId(null);
        }
        client.setCreatedDate(LocalDateTime.now());
        Client clientSaved = clientRepository.save(client);
        return clientMapper.clientToClientDto(clientSaved);
    }

    @Override
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public ClientDto updateClient(ClientDto clientDto) {
        return addClient(clientDto);
    }

    @Override
    public Page<ClientDto> getClients(Long companyId, Pageable pageable, GenericFilterDto filterDto) {
        Specification<Client> clientSpecification = ClientSpecification.buildComplexSpecification(companyId, null, filterDto);
        Page<Client> clientList = clientRepository.findAll(clientSpecification, pageable);
        return clientMapper.clientsPagetoClientsDtoPage(clientList);
    }

    @Override
    public ClientDto getClientById(String clientId) {
        Optional<Client> client = clientRepository.findByClientId(clientId);

        return client.map(clientMapper::clientToClientDto).orElse(null);
    }

    @Override
    public ClientDto getClientByIdAndCompany(Long companyId, String clientId) {
        Specification<Client> clientSpecification = ClientSpecification.buildComplexSpecification(companyId, clientId, new GenericFilterDto());
        Optional<Client> client = clientRepository.findOne(clientSpecification);
        return client.map(clientMapper::clientToClientDto).orElse(null);
    }
}
