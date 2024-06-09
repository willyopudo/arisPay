package org.arispay.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.GenericHttpResponse;
import org.arispay.entity.User;
import org.arispay.data.dtoauth.JwtLoginReq;
import org.arispay.data.dtoauth.JwtLoginResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/auth")
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
	public ResponseEntity login(@RequestBody JwtLoginReq loginReq) {

		try {
			Authentication authentication =
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
			String username = authentication.getName();
			User user = new User();
			user.setUsername(username);
			String token = jwtUtil.createToken(user);
			JwtLoginResp loginRes = new JwtLoginResp(token,3600, "Bearer");

            logger.info("Token issued success for user: {} , Token : {}", username, token);
			return ResponseEntity.ok(loginRes);

		} catch (BadCredentialsException e) {
			GenericHttpResponse genericHttpResponse = new GenericHttpResponse(HttpStatus.BAD_REQUEST, "Invalid credentials");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericHttpResponse);
		} catch (Exception e) {
			GenericHttpResponse genericHttpResponse = new GenericHttpResponse(HttpStatus.BAD_REQUEST, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericHttpResponse);
		}
	}
}
