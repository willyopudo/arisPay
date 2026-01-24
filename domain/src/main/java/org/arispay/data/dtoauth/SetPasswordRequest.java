package org.arispay.data.dtoauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SetPasswordRequest {
    private String token;
    private String password;
}
