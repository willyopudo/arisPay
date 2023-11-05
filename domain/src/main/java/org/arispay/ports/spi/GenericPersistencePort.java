package org.arispay.ports.spi;

import java.util.List;

public interface GenericPersistencePort<T> {
	T add(T obj);

	void deleteById(Long id);

	T update(T obj);

	List<T> getAll();

	T getById(Long id);
}
