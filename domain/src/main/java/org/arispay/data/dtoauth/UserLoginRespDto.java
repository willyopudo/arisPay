package org.arispay.data.dtoauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.arispay.data.UserCompanyDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginRespDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("roles")
    private List<String> authorities;

    @JsonProperty("role")
    private String role;

    private Long companyId;

}
