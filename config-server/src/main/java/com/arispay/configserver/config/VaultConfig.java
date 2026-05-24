package com.arispay.configserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;

import java.net.URI;

/**
 * Phase 2 – HashiCorp Vault integration.
 * <p>
 * Provides the {@link VaultEndpoint} and {@link ClientAuthentication} beans
 * used by Spring Vault to connect to the Vault server.
 * <p>
 * Token is injected from the {@code VAULT_TOKEN} environment variable
 * (defaults to {@code arispay-dev-token} for local development).
 */
@Configuration
public class VaultConfig extends AbstractVaultConfiguration {

    @Value("${spring.cloud.vault.host:localhost}")
    private String vaultHost;

    @Value("${spring.cloud.vault.port:8200}")
    private int vaultPort;

    @Value("${spring.cloud.vault.scheme:http}")
    private String vaultScheme;

    @Value("${spring.cloud.vault.token:arispay-dev-token}")
    private String vaultToken;

    @Override
    public VaultEndpoint vaultEndpoint() {
        return VaultEndpoint.from(URI.create(vaultScheme + "://" + vaultHost + ":" + vaultPort));
    }

    @Override
    public ClientAuthentication clientAuthentication() {
        return new TokenAuthentication(vaultToken);
    }
}

