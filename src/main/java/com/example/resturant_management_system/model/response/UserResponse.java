package com.example.resturant_management_system.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

	private Long id;
	private String name;
	private String email;
	private String phone;
	private String role;
}