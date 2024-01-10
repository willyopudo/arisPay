package org.arispay.adapters;

import org.arispay.data.ClientDto;
import org.arispay.entity.Client;
import org.arispay.mappers.ClientMapper;
import org.arispay.ports.spi.ClientPersistencePort;
import org.arispay.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ClientDto> getClients() {
        List<Client> clientList = clientRepository.findAll();

        return clientMapper.clientListToClientDtoList(clientList);
    }

    @Override
    public ClientDto getClientById(Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);


        return client.map(clientMapper::clientToClientDto).orElse(null);
    }
}
