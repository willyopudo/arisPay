package org.arispay.ports.api;

import org.arispay.data.GenericFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyAccountServicePort<T> extends GenericServicePort<T> {
    T getByAccountNumber(String accountNumber);

    Page<T> getAll(Long companyId, Pageable pageable, GenericFilterDto filterDto);
}
