package org.arispay.ports.spi;

import org.arispay.data.ClientDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientPersistencePort {
    ClientDto addClient(ClientDto clientDto);

    void deleteClientById(Long id);

    ClientDto updateClient(ClientDto clientDto);


    Page<ClientDto> getClients();

    ClientDto getClientById(String clientId);

    ClientDto getClientByIdAndCompany(Long companyId, String clientId);
}
