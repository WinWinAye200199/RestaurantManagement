package com.example.resturant_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.resturant_management_system.security.JwtAuthenticationEntryPoint;
import com.example.resturant_management_system.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
   AuthenticationManager authenticationManager(HttpSecurity httpSecurity, 
														UserDetailsService userDetailsService, 
														PasswordEncoder passwordEncoder) throws Exception{
		//Get AuthManager Builder
		AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		//Set UserDetail Service and PasswordEncoder
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		//Build AuthManager
		AuthenticationManager authenticationManager = builder.build();
		
		return authenticationManager;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.cors();
	    http.csrf().disable();
	    http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	        .authorizeHttpRequests(authorize -> authorize
	            .antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
	            .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll() // Permission for signup
	            .antMatchers(HttpMethod.PUT, "/api/auth/reset-password").permitAll()
	            .antMatchers(HttpMethod.POST, "/api/auth/request-reset-password").permitAll()
	            .antMatchers("/api/users/**").hasAnyRole("USER","ADMIN")
	            .antMatchers("/api/admin/**").hasRole("ADMIN")
	            .anyRequest().authenticated());
	    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}


}