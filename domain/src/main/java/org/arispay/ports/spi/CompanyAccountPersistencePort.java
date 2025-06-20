package org.arispay.ports.spi;

import org.arispay.data.GenericFilterDto;
import org.arispay.data.SelectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyAccountPersistencePort<T> extends GenericPersistencePort<T>{
    T getByAccountNumber(String accountNumber);

    Page<T> getAll(Long companyId, Pageable pageable, GenericFilterDto filterDto);

    //Gets a list of accounts for a company to be used in select dropdowns
    List<SelectDto> getAccountsSelectList(Long companyId);
}
