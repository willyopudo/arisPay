package org.arispay.ports.spi;

import org.arispay.data.GenericFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyAccountPersistencePort<T> extends GenericPersistencePort<T>{
    T getByAccountNumber(String accountNumber);

    Page<T> getAll(Long companyId, Pageable pageable, GenericFilterDto filterDto);
}
