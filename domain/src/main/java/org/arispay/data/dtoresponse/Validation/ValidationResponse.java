package org.arispay.data.dtoresponse.Validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {

    private String status_code;
    private String status_description;
    private String date_time;
    private Payload payload;
}
