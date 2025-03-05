package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterDto {
    private String status;
    private String role;
    private String currentPlan;
    private String search;
    private Sort.Direction direction = Sort.Direction.ASC;
    private String sortBy = "firstName";
}
