package com.example.resturant_management_system.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.resturant_management_system.service.Impl.JwtServiceImpl;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtServiceImpl jwtService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//retrieve claims from request header
		Claims claims = jwtService.resolveClaims(request);
		
		if(claims != null) {
			//create authentication 
			//Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getId(),"",null);
			String username = claims.getSubject();
			String email = claims.getSubject();
			String phone = claims.getSubject();
			String roles = claims.get("roles",String.class);
			Long userId = Long.parseLong(claims.getId());
			List<String> authorityArray = Arrays.asList(roles.split(","));
			List<GrantedAuthority> authorities = authorityArray.stream()
					.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
			UserPrincipal userPrincipal = new UserPrincipal(userId, username , email, phone, null, authorities);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);//save to security context
		}
		
		//continue filter chain
		filterChain.doFilter(request, response);
		
	}

}