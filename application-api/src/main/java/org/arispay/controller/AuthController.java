package org.arispay.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.GenericHttpResponse;
import org.arispay.data.dtoauth.UserLoginRespDto;
import org.arispay.data.dtoauth.WebLoginResponse;
import org.arispay.entity.User;
import org.arispay.data.dtoauth.JwtLoginReq;
import org.arispay.data.dtoauth.JwtLoginResp;
import org.arispay.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	@Autowired
	private final AuthenticationManager authenticationManager;
	private static final Logger logger = LogManager.getLogger(AuthController.class);


	private final JwtUtil jwtUtil;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;

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
			UserLoginRespDto userDetail = new UserLoginRespDto(userDetails.getId(), userDetails.getUsername(), userDetails.getUsername(), userDetails.getEmail(), "avatar-1.png", userDetails.getAuthoritiesList(), userDetails.getAuthoritiesList().getFirst());

			WebLoginResponse webLoginResp = new WebLoginResponse(token, 3600, "Bearer", userDetail);
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
}
