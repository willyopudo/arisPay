package org.arispay.ports.api;

import org.arispay.data.ClientDto;

import java.util.List;

public interface ClientServicePort {
    ClientDto addClient(ClientDto clientDto);

    void deleteClientById(Long id);

    ClientDto updateClient(ClientDto clientDto);

    List<ClientDto> getClients();

    ClientDto getClientById(Long id);
}
