package org.arispay.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.arispay.data.validation.OnAdminUpdate;
import org.arispay.data.validation.OnUserUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	private Long id;

	@NotBlank(message = "Username should not be blank", groups = {OnAdminUpdate.class})
	private String username;

	@NotBlank(message = "First name should not be blank", groups = {OnUserUpdate.class, OnAdminUpdate.class})
	private String firstName;

	@NotBlank(message = "Last name should not be blank", groups = {OnUserUpdate.class, OnAdminUpdate.class})
	private String lastName;

	@Email(message = "Email should be valid", groups = {OnUserUpdate.class, OnAdminUpdate.class})
	@NotBlank(message = "Email should not be blank", groups = {OnUserUpdate.class, OnAdminUpdate.class})
	private String email;

	@NotNull(message = "User Companies can not be empty", groups = {OnAdminUpdate.class})
	private List<UserCompanyDto> userCompanies = new ArrayList<>();

	//@NotEmpty(message = "Password should not be empty")
	private String password;

	@NotBlank(message = "Phone number should not be blank", groups = {OnUserUpdate.class, OnAdminUpdate.class})
	private String phoneNumber;

	@NotBlank(message = "Address should not be blank", groups = {OnUserUpdate.class, OnAdminUpdate.class})
	private String address;

	private String zipCode;

	@NotBlank(message = "Town should not be blank", groups = {OnUserUpdate.class, OnAdminUpdate.class})
	private String town;

	// Admin-only fields
	@NotBlank(message = "Role should not be blank", groups = {OnAdminUpdate.class})
	private String role;

	@NotBlank(message = "Status should not be blank", groups = {OnAdminUpdate.class})
	private String status;

	private String currentPlan;

	private String avatar;

	private String token;

	private LocalDateTime tokenExpiration;
}
