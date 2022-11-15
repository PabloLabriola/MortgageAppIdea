package com.cognizant.companymanagement.security.jwt;

import com.cognizant.companymanagement.security.enumeration.TokenStateEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JwtUtils {

	@Value("${companymgt.app.jwtSecret}")
	private String jwtSecret;

	@Value("${companymgt.app.jwtSecretRefresh}")
	private String jwtSecretRefresh;

	@Value("${companymgt.app.jwtExpirationMs}")
	private Integer jwtExpirationMs;
	
	@Value("${companymgt.app.jwtRefreshExpirationMs}")
	private Integer jwtRefreshTokenDurationMs;

	private Key key;
	private Key refreshKey;

	@PostConstruct
	public void init(){

		this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		this.refreshKey = Keys.hmacShaKeyFor(jwtSecretRefresh.getBytes());
	}

	/**
	 * Given a company name, a new jwt access token is generated with a secret and an expiration
	 * time
	 * 
	 * @param companyName
	 * @return
	 */
	public String getJWTAccessToken(String companyName) {
		return generateJwt(companyName, jwtExpirationMs);
	}

	/**
	 * Given a company name, a new refresh token jwt is generated with a secret and an expiration
	 * time
	 *
	 * @param companyName
	 * @return
	 */
	public String getJWTRefreshToken(String companyName) {
		return generateJwtRefresh(companyName, jwtRefreshTokenDurationMs);
	}

	public String getCompanyNameFromJwtRefreshToken(String token) {
		return Jwts.parserBuilder().setSigningKey(jwtSecretRefresh.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public TokenStateEnum checkRefreshToken(String token){
		return checkJwtToken(token, jwtSecretRefresh);
	}

	public TokenStateEnum checkAccessToken(String token){
		return checkJwtToken(token, jwtSecret);
	}

	/**
	 * Validate whether a token is signed by company-management, well formed
	 * and not expired
	 * @param authToken
	 * @return
	 */
	public TokenStateEnum checkJwtToken(String authToken, String secret) {
		try {
			String BEARER_PREFIX = "Bearer ";
			String jwt = authToken.replaceAll(BEARER_PREFIX, "");
			Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(jwt);
			return TokenStateEnum.VALID;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
			return TokenStateEnum.INVALID;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			return TokenStateEnum.MALFORMED;
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			return TokenStateEnum.EXPIRED;
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
			return TokenStateEnum.UNSUPPORTED;
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			return TokenStateEnum.UNSUPPORTED;
		}
	}
	
	/**
	 * Return a new created token from companyName, secretKey and expiration time
	 * 
	 * @param companyName
	 * @param expiration
	 * @return
	 */
	private String generateJwt(String companyName, Integer expiration) {
		return Jwts.builder().setSubject(companyName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(key).compact();
	}

	/**
	 * Return a new created Refresh token from companyName, secretKey and expiration time
	 *
	 * @param companyName
	 * @param expiration
	 * @return
	 */
	private String generateJwtRefresh(String companyName, Integer expiration) {
		return Jwts.builder().setSubject(companyName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(refreshKey).compact();
	}
}
