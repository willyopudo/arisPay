package org.arispay.service;

import org.arispay.data.ClientDto;
import org.arispay.ports.api.ClientServicePort;
import org.arispay.ports.spi.ClientPersistencePort;

import java.util.List;

public class ClientServiceImpl implements ClientServicePort {

	private ClientPersistencePort clientPersistencePort;

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
	public List<ClientDto> getClients() {
		return clientPersistencePort.getClients();
	}

	@Override
	public ClientDto getClientById(String id) {
		return clientPersistencePort.getClientById(id);
	}
}
