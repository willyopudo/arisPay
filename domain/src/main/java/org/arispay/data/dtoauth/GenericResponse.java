package org.arispay.data.dtoauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericResponse {
    private String responseCode;
    private String responseMessage;
}
