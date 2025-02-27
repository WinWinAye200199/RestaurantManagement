package com.example.resturant_management_system.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtResponse {

	private String type;
	private String accessToken;
	private String expiredAt;
	private String role;
}