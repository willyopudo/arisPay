package org.arispay.controller;

import org.arispay.auth.JwtUtil;
import org.arispay.entity.User;
import org.arispay.data.dtorequest.JwtLoginReq;
import org.arispay.data.dtoresponse.ErrorResp;
import org.arispay.data.dtoresponse.JwtLoginResp;
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
@RequestMapping("/rest/auth")
public class AuthController {

	@Autowired
	private final AuthenticationManager authenticationManager;


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

			return ResponseEntity.ok(loginRes);

		} catch (BadCredentialsException e) {
			ErrorResp errorResponse = new ErrorResp(HttpStatus.BAD_REQUEST, "Invalid username or password");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} catch (Exception e) {
			ErrorResp errorResponse = new ErrorResp(HttpStatus.BAD_REQUEST, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
}
