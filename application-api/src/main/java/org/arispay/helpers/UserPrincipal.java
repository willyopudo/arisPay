package org.arispay.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

@Data
@AllArgsConstructor
public class UserPrincipal implements Principal {
    private final String name;
    private final Long companyId;
}
