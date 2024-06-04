package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenericHttpResponse {
	HttpStatus httpStatus;
	String message;
}
