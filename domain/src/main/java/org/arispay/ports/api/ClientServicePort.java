package org.arispay.ports.api;

import org.arispay.data.ClientDto;
import org.arispay.data.GenericFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientServicePort {
    ClientDto addClient(ClientDto clientDto);

    void deleteClientById(Long id);

    ClientDto updateClient(ClientDto clientDto);

    Page<ClientDto> getClients(Long companyId, Pageable pageable, GenericFilterDto filterDto);

    ClientDto getClientById(String id);

    ClientDto getClientByIdAndCompany(Long companyId, String clientId);
}
