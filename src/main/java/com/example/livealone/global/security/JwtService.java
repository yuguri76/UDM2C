package com.example.livealone.global.security;

import com.example.livealone.user.entity.User;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class JwtService {

	public static final String HEADER = "Authorization";

	private static final String TOKEN_PREFIX = "Bearer ";

	public static final String CLAIM_ID = "id";
	public static final String CLAIM_USERNAME = "username";
	public static final String CLAIM_NICKNAME = "nickname";
	public static final String CLAIM_ROLE ="role";
	@Value("${jwt.key}")
	private String SECRET_KEY;

	@Value("${jwt.access-expire-time}")
	private Long EXPIRE_TIME;

	private Key key;

	@PostConstruct
	public void init() {

		byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
		key = Keys.hmacShaKeyFor(bytes);

	}

	public String generateToken(User user) {

		Date curDate = new Date();
		Date expireDate = new Date(curDate.getTime() + EXPIRE_TIME);

		return TOKEN_PREFIX + Jwts.builder()
			.setSubject(user.getEmail())
			.claim(CLAIM_ID, user.getId())
			.claim(CLAIM_USERNAME, user.getUsername())
			.claim(CLAIM_NICKNAME, user.getNickname())
			.claim(CLAIM_ROLE,user.getRole())
			.setIssuedAt(curDate)
			.setExpiration(expireDate)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

	}

	public boolean isValidToken(String token, HttpServletRequest request) {

		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
			request.setAttribute("error", "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			request.setAttribute("error", "Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			request.setAttribute("error", "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			request.setAttribute("error", "JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}

		return false;

	}

	public String isValidToken(String token){
		try{
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return "Valid";
		}catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
			log.debug("error :", "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
			return "Invalid JWT signature";
		} catch (ExpiredJwtException e) {
			log.debug("error :", "Expired JWT token, 만료된 JWT token 입니다.");
			return "Expired JWT Token";
		} catch (UnsupportedJwtException e) {
			log.debug("error :", "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
			return "Unsupported JWT Token";
		} catch (IllegalArgumentException e) {
			log.debug("error :", "JWT claims is empty, 잘못된 JWT 토큰 입니다.");
			return "JWT Claims is empty";
		}
	}

	public String getToken(HttpServletRequest request) {

		String token = request.getHeader(HEADER);

		if(StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX, "");
		}
		return null;

	}

	public Claims getClaims(String token) {

		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

	}

}
