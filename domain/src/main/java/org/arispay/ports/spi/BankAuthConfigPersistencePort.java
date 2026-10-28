package org.arispay.ports.spi;

import org.arispay.data.BankAuthConfigDto;

import java.util.List;

public interface BankAuthConfigPersistencePort {
    List<BankAuthConfigDto> getAllActiveConfigs();
    BankAuthConfigDto getByBankCode(String bankCode);
}
