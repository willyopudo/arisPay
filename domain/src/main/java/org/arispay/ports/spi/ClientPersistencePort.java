package org.arispay.ports.spi;

import org.arispay.data.ClientDto;

import java.util.List;

public interface ClientPersistencePort {
    ClientDto addClient(ClientDto clientDto);

    void deleteClientById(Long id);

    ClientDto updateClient(ClientDto clientDto);

    List<ClientDto> getClients();

    ClientDto getClientById(String clientId);
}
