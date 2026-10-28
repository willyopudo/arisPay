package org.arispay.adapters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.BankEndpointDto;
import org.arispay.entity.BankEndpoint;
import org.arispay.ports.spi.BankEndpointPersistencePort;
import org.arispay.repository.BankEndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BankEndpointJpaAdapter implements BankEndpointPersistencePort {

    private static final Logger logger = LogManager.getLogger(BankEndpointJpaAdapter.class);
    private final Map<String, String> endpointCache = new ConcurrentHashMap<>();

    @Autowired
    private BankEndpointRepository bankEndpointRepository;

    @Override
    public String getEndpointUrl(String bankCode, String endpointName) {
        String cacheKey = bankCode + ":" + endpointName;
        return endpointCache.computeIfAbsent(cacheKey, key -> {
            logger.debug("Fetching endpoint URL from DB: bankCode={}, endpointName={}", bankCode, endpointName);
            return bankEndpointRepository.findByBankCodeAndEndpointNameAndActiveTrue(bankCode, endpointName)
                    .map(BankEndpoint::getEndpointUrl)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No active endpoint found for bankCode=" + bankCode + ", endpointName=" + endpointName));
        });
    }

    @Override
    public List<BankEndpointDto> getEndpoints(String bankCode) {
        return bankEndpointRepository.findByBankCodeAndActiveTrue(bankCode).stream()
                .map(this::toDto)
                .toList();
    }

    public void evictCache() {
        endpointCache.clear();
        logger.info("Bank endpoints cache evicted");
    }

    private BankEndpointDto toDto(BankEndpoint entity) {
        return BankEndpointDto.builder()
                .id(entity.getId())
                .bankCode(entity.getBankCode())
                .endpointName(entity.getEndpointName())
                .endpointUrl(entity.getEndpointUrl())
                .description(entity.getDescription())
                .active(entity.isActive())
                .build();
    }
}
