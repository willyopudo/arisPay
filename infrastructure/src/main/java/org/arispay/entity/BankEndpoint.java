package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_endpoint")
public class BankEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @Column(name = "endpoint_name", nullable = false)
    private String endpointName;

    @Column(name = "endpoint_url", nullable = false)
    private String endpointUrl;

    private String description;

    @Column(nullable = false)
    private boolean active = true;
}
