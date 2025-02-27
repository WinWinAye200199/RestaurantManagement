package com.example.resturant_management_system.service.Impl;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.resturant_management_system.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtServiceImpl {

	@Value("${app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${app.jwtExpiration}")
	private int jwtExpiration;// one day


	public String createToken(Authentication authentication) {

		Date tokenValidity = new Date(System.currentTimeMillis() + jwtExpiration * 1000);

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		
		final String authorities = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return Jwts.builder().setId(Long.toString(userPrincipal.getId())).setSubject((userPrincipal.getUsername()))
				.claim("roles", authorities).setIssuedAt(new Date())
				.setExpiration(tokenValidity)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Claims resolveClaims(HttpServletRequest request) {
		String token = getToken(request);

		JwtParser jwtParser = Jwts.parser().setSigningKey(jwtSecret);
		try {
			if (token != null) {
				
				Claims claims = jwtParser.parseClaimsJws(token).getBody();
				return claims;
			}
			return null;
			// save to security context
		} catch (ExpiredJwtException ex) {
			request.setAttribute("expired", ex.getMessage());
			throw ex;
		}
	}

	private String getToken(HttpServletRequest request) {

		String value = request.getHeader("Authorization");
		if (value != null && value.startsWith("Bearer ")) {
			return value.replace("Bearer ", "");
		}

		return null;
		
		
	}
}