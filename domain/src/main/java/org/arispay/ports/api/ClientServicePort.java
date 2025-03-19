package org.arispay.ports.api;

import org.arispay.data.ClientDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientServicePort {
    ClientDto addClient(ClientDto clientDto);

    void deleteClientById(Long id);

    ClientDto updateClient(ClientDto clientDto);

    Page<ClientDto> getClients();

    ClientDto getClientById(String id);

    ClientDto getClientByIdAndCompany(Long companyId, String clientId);
}
