package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_auth_config")
public class BankAuthConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @Column(name = "partner_name", nullable = false)
    private String partnerName;

    @Column(name = "url_pattern", nullable = false)
    private String urlPattern;

    @Column(name = "auth_url", nullable = false)
    private String authUrl;

    @Column(nullable = false)
    private String strategy;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    private String scope;

    @Column(name = "body_format", nullable = false)
    private String bodyFormat = "FORM";

    @Column(name = "token_cache_duration", nullable = false)
    private long tokenCacheDuration = 3300;

    @Column(nullable = false)
    private boolean active = true;
}
