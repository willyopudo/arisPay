package org.arispay.ports.spi;

import org.arispay.data.ClientDto;
import org.arispay.data.GenericFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientPersistencePort {
    ClientDto addClient(ClientDto clientDto);

    void deleteClientById(Long id);

    ClientDto updateClient(ClientDto clientDto);


    Page<ClientDto> getClients(Long companyId, Pageable pageable, GenericFilterDto filterDto);

    ClientDto getClientById(String clientId);

    ClientDto getClientByIdAndCompany(Long companyId, String clientId);
}
