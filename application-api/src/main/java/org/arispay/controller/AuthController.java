package org.arispay.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.adapters.auth.RefreshTokenService;
import org.arispay.auth.JwtUtil;
import org.arispay.data.*;
import org.arispay.data.dtoauth.*;
import org.arispay.entity.User;
import org.arispay.entity.UserCompany;
import org.arispay.entity.auth.RefreshToken;
import org.arispay.exception.TokenRefreshException;
import org.arispay.helpers.AuthUtil;
import org.arispay.mappers.UserMapper;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.UserServicePort;
import org.arispay.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	@Autowired
	private final AuthenticationManager authenticationManager;

	@Autowired
	RefreshTokenService refreshTokenService;

	private static final Logger logger = LogManager.getLogger(AuthController.class);


	private final JwtUtil jwtUtil;

	private final PasswordEncoder passwordEncoder;

	private final UserServicePort userServicePort;

	private final CompanyServicePort companyServicePort;

	private final CompanyAccountServicePort<CompanyAccountDto> companyAccountServicePort;

	private final UserMapper userMapper;


	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserServicePort userServicePort,
						  CompanyServicePort companyServicePort, CompanyAccountServicePort<CompanyAccountDto> companyAccountServicePort, UserMapper userMapper) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
		this.userServicePort = userServicePort;
		this.companyServicePort = companyServicePort;
		this.companyAccountServicePort = companyAccountServicePort;
		this.userMapper = userMapper;
	}

	// Register new user
	@PostMapping("/register")
	public ResponseEntity<GenericHttpResponse<?>> register(@Valid @RequestBody RegistrationDto registrationDto,
														   BindingResult result,
														   Model model, Principal principal) {
		registrationDto.getUserDto().setId(null);
		GenericHttpResponse<RegistrationDto> response = new GenericHttpResponse<>();
		UserDto existingUser = userServicePort.findUserByEmail(registrationDto.getUserDto().getEmail());

		return AuthUtil.registerCompanyAdmin(registrationDto, result, response, existingUser, logger, passwordEncoder, userServicePort, companyServicePort, companyAccountServicePort);
	}


	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody JwtLoginReq loginReq) {

		try {
			Authentication authentication =
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
			String username = authentication.getName();
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

			User user = new User();
			user.setUsername(username);
			String token = jwtUtil.createToken(user);
			List<UserCompany> userCompanies = userDetails.getUserCompanies().stream().filter(UserCompany::isDefault).toList();
			UserCompany userCompany = userCompanies.stream().findFirst().get();

			//Build UserDetails object
			UserLoginRespDto userDetail = new UserLoginRespDto(userDetails.getId(), userDetails.getUsername(), userDetails.getFullName(), userDetails.getEmail(), userDetails.getId() + ".png", userDetails.getAuthoritiesList(), userDetails.getAuthoritiesList().getFirst().substring(5),
					userCompany.getCompany().getId());

			//Generate refresh token
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

			//Login Response for web client requests
			WebLoginResponse webLoginResp = new WebLoginResponse(token, refreshToken.getToken(), 3600, "Bearer", userDetail);

			//Login response for non web requests
			JwtLoginResp loginRes = new JwtLoginResp(token,3600, "Bearer");

            logger.info("Token issued success for user: {} , Token : {}", username, token);
			if(loginReq.getScope() != null && loginReq.getScope().equals("web")) {
				return ResponseEntity.ok(webLoginResp);
			}
			return ResponseEntity.ok(loginRes);

		} catch (BadCredentialsException e) {
			GenericHttpResponse<?> genericHttpResponse = new GenericHttpResponse<String>(HttpStatus.BAD_REQUEST, "Invalid credentials",null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericHttpResponse);
		} catch (Exception e) {
			GenericHttpResponse<?> genericHttpResponse = new GenericHttpResponse<String>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericHttpResponse);
		}
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtUtil.createToken(user);
					return ResponseEntity.ok(new TokenRefreshResponse(200, token, requestRefreshToken, "Bearer"));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
						"Refresh token is not in database!"));
	}

	@PostMapping("/set-password")
	public ResponseEntity<?> setPassword(@Valid @RequestBody SetPasswordRequest setPasswordRequest) {
		try {
			UserDto userDto = userServicePort.setPassword(setPasswordRequest.getToken(), setPasswordRequest.getPassword());
			return ResponseEntity.ok(userDto);
		}
		catch (Exception e) {
			GenericHttpResponse<?> genericHttpResponse = new GenericHttpResponse<String>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericHttpResponse);
		}

	}

	@GetMapping("/test-controller")
	public RegistrationDto returnObjectInBrowser() {
		RegistrationDto someClass = new RegistrationDto();
		someClass.setCompanyAccountDto(new CompanyAccountDto());
		someClass.setUserDto(new UserDto());
		someClass.setCompanyDto(new CompanyDto());
		someClass.getUserDto().getUserCompanies().add(new UserCompanyDto());
		return someClass;
	}
}
