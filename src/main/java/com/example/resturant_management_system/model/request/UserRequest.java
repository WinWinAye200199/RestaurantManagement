package com.example.resturant_management_system.model.request;

import javax.validation.constraints.NotBlank;

import com.example.resturant_management_system.model.entities.JobRole;
import com.example.resturant_management_system.model.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotBlank
public class UserRequest {

	private String name;
	private String email;
	private String phone;
	private String password;
	private String confirmedPassword; 
	private Role role; // Either ADMIN or STAFF
    private JobRole jobRole; // Optional, only for STAFF
	
}