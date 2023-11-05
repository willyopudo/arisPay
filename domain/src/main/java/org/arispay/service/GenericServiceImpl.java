package org.arispay.service;

import lombok.RequiredArgsConstructor;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.ports.spi.GenericPersistencePort;

import java.util.List;

@RequiredArgsConstructor
public class GenericServiceImpl<T> implements GenericServicePort<T> {
	private GenericPersistencePort<T> genericPersistencePort;

	@Override
	public T add(T obj) {
		return genericPersistencePort.add(obj);
	}

	@Override
	public void deleteById(Long id) {
		genericPersistencePort.deleteById(id);
	}

	@Override
	public T update(T obj) {
		return genericPersistencePort.update(obj);
	}

	@Override
	public List<T> getAll() {
		return genericPersistencePort.getAll();
	}

	@Override
	public T getById(Long id) {
		return genericPersistencePort.getById(id);
	}

}
