package org.arispay.adapters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.BankAuthConfigDto;
import org.arispay.entity.BankAuthConfig;
import org.arispay.ports.spi.BankAuthConfigPersistencePort;
import org.arispay.repository.BankAuthConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BankAuthConfigJpaAdapter implements BankAuthConfigPersistencePort {

    private static final Logger logger = LogManager.getLogger(BankAuthConfigJpaAdapter.class);
    private final Map<String, Object> configCache = new ConcurrentHashMap<>();
    private static final String ALL_CONFIGS_KEY = "__all__";

    @Autowired
    private BankAuthConfigRepository bankAuthConfigRepository;

    @Override
    @SuppressWarnings("unchecked")
    public List<BankAuthConfigDto> getAllActiveConfigs() {
        return (List<BankAuthConfigDto>) configCache.computeIfAbsent(ALL_CONFIGS_KEY, key -> {
            logger.debug("Fetching all active bank auth configs from DB");
            return bankAuthConfigRepository.findByActiveTrue().stream()
                    .map(this::toDto)
                    .toList();
        });
    }

    @Override
    public BankAuthConfigDto getByBankCode(String bankCode) {
        return (BankAuthConfigDto) configCache.computeIfAbsent("bank:" + bankCode, key -> {
            logger.debug("Fetching bank auth config from DB for bankCode={}", bankCode);
            return bankAuthConfigRepository.findByBankCodeAndActiveTrue(bankCode)
                    .map(this::toDto)
                    .orElse(null);
        });
    }

    public void evictCache() {
        configCache.clear();
        logger.info("Bank auth configs cache evicted");
    }

    private BankAuthConfigDto toDto(BankAuthConfig entity) {
        return BankAuthConfigDto.builder()
                .id(entity.getId())
                .bankCode(entity.getBankCode())
                .partnerName(entity.getPartnerName())
                .urlPattern(entity.getUrlPattern())
                .authUrl(entity.getAuthUrl())
                .strategy(entity.getStrategy())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .scope(entity.getScope())
                .bodyFormat(entity.getBodyFormat())
                .tokenCacheDuration(entity.getTokenCacheDuration())
                .active(entity.isActive())
                .build();
    }
}
