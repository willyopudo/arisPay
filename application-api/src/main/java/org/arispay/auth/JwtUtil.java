package org.arispay.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.controller.UserController;
import org.arispay.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.arispay.entity.UserCompany;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
	private static final Logger logger = LogManager.getLogger(JwtUtil.class);

	private final String secretKey;

	@Value("${custom.arispay.app.jwt-expiration-minutes}")
	private  int accessTokenValidity;
	//private final String secret_key = "GFYGHI5342@#$%65287629hshgERRjy2789}-0uhuyTUUWFR4";
	//protected final long accessTokenValidity = 60;

	private final JwtParser jwtParser;

	protected final String TOKEN_HEADER = "Authorization";
	protected final String TOKEN_PREFIX = "Bearer ";

	public JwtUtil(@Value("${custom.arispay.app.jwt-secret}") String secretKey) {
		this.secretKey = secretKey;
		this.jwtParser = Jwts.parser().setSigningKey(this.secretKey);
	}

	public String createToken(User user) {
		Claims claims = Jwts.claims().setSubject(user.getUsername());

		claims.put("companyId", user.getUserCompanies().stream()
				.filter(UserCompany::isDefault)
				.map(userCompany -> userCompany.getCompany().getId())
				.findFirst()
				.orElse(null));
		claims.put("firstName", user.getFirstName());
		claims.put("lastName", user.getLastName());

		Date tokenCreateTime = new Date();
		Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(tokenValidity)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	private Claims parseJwtClaims(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public Claims resolveClaims(HttpServletRequest req) {
		try {
			String token = resolveToken(req);
			if (token != null) {
				return parseJwtClaims(token);
			}
			return null;
		} catch (ExpiredJwtException ex) {
			req.setAttribute("expired", ex.getMessage());
            logger.error("Token expired {}", ex.getMessage());
			throw ex;
		} catch (SignatureException ex) {
			req.setAttribute("invalid", ex.getMessage());
			logger.error("Invalid JWT signature: {}", ex.getMessage());
			throw ex;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			throw e;
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
			throw e;
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
			throw e;
		}
    }

	public String resolveToken(HttpServletRequest request) {

		String bearerToken = request.getHeader(TOKEN_HEADER);
		if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(TOKEN_PREFIX.length());
		}
		return null;
	}

	public boolean validateClaims(Claims claims) throws AuthenticationException {
		try {
			return claims.getExpiration().after(new Date());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public String getEmail(Claims claims) {
		return claims.getSubject();
	}

	private List<String> getRoles(Claims claims) {
		return (List<String>) claims.get("roles");
	}


}