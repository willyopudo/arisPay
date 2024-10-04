package org.arispay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserCompanyId implements Serializable {
    @Column(name = "user_id")
    private Long user;
    @Column(name = "company_id")
    private Long company;
}
