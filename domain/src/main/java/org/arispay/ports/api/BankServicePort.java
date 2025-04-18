package org.arispay.ports.api;

import org.arispay.data.ClientDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.data.SelectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BankServicePort {
    List<SelectDto> getBanks();

}
