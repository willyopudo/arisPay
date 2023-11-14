package org.arispay.data.dtorequest;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtLoginReq {
	private String username;
	private String password;
}
