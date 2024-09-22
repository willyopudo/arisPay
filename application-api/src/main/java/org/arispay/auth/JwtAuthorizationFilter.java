package org.arispay.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtUtil jwtUtil;
	private final ObjectMapper mapper;

	public JwtAuthorizationFilter(HandlerExceptionResolver handlerExceptionResolver, JwtUtil jwtUtil, ObjectMapper mapper) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtUtil = jwtUtil;
		this.mapper = mapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Map<String, Object> errorDetails = new HashMap<>();

		try {
			String accessToken = jwtUtil.resolveToken(request);
			if (accessToken == null) {
				filterChain.doFilter(request, response);
				return;
			}
			//System.out.println("token : " + accessToken);
			Claims claims = jwtUtil.resolveClaims(request);

			if (claims != null & jwtUtil.validateClaims(claims)) {
				String username = claims.getSubject();
				System.out.println("username : " + username);
				Authentication authentication =
						new UsernamePasswordAuthenticationToken(username, "", new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}


			filterChain.doFilter(request, response);
		}
		catch (Exception e) {
			handlerExceptionResolver.resolveException(request, response, null, e);

		}
	}
}
