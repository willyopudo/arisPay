package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericFilterDto {
    private List<String> filters;
    private String search;
    private Sort.Direction direction = Sort.Direction.ASC;
    private String sortBy;
}
