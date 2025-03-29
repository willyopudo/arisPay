package org.arispay.service;

import org.arispay.data.ClientDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.ports.api.ClientServicePort;
import org.arispay.ports.spi.ClientPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ClientServiceImpl implements ClientServicePort {

	private final ClientPersistencePort clientPersistencePort;

	public ClientServiceImpl(ClientPersistencePort clientPersistencePort) {
		this.clientPersistencePort = clientPersistencePort;
	}

	@Override
	public ClientDto addClient(ClientDto clientDto) {
		return clientPersistencePort.addClient(clientDto);
	}

	@Override
	public void deleteClientById(Long id) {
		clientPersistencePort.deleteClientById(id);
	}

	@Override
	public ClientDto updateClient(ClientDto clientDto) {
		return clientPersistencePort.updateClient(clientDto);
	}

	@Override
	public Page<ClientDto> getClients(Long companyId, Pageable pageable, GenericFilterDto filterDto) {
		return clientPersistencePort.getClients(companyId, pageable, filterDto);
	}

	@Override
	public ClientDto getClientById(String id) {
		return clientPersistencePort.getClientById(id);
	}

	@Override
	public ClientDto getClientByIdAndCompany(Long companyId, String clientId) {
		return clientPersistencePort.getClientByIdAndCompany(companyId, clientId);
	}
}
