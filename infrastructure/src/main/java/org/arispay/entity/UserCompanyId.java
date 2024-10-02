package org.arispay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UserCompanyId implements Serializable {
    @Column(name = "user_id")
    private Long user;
    @Column(name = "company_id")
    private Long company;
}
