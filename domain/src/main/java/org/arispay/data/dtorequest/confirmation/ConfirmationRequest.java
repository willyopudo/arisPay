package org.arispay.data.dtorequest.confirmation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationRequest {
    private String action;
    private Payload payload;
}
