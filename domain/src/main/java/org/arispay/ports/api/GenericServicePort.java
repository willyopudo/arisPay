package org.arispay.ports.api;

import org.arispay.data.CompanyDto;

import java.util.List;

public interface GenericServicePort<T> {
	T add(T obj);

	void deleteById(Long id);

	T update(T obj);

	List<T> getAll();

	T getById(Long id);
}
