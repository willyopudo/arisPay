package org.arispay.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserCompanyId implements Serializable {
    private Long user;
    private Long company;
}
