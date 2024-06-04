package org.arispay.data.dtoauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtLoginReq {

	@JsonProperty("client_id")
	private String username;
	@JsonProperty("client_secret")
	private String password;
	@JsonProperty("grant_type")
	private String grantType;
	@JsonProperty("scope")
	private String scope;
}
