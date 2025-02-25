package com.example.resturant_management_system.model.request;

import com.example.resturant_management_system.model.entities.JobRole;
import com.example.resturant_management_system.model.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
	private String name;
	private String email;
	private String phone;
	private Role role; // Either ADMIN or STAFF
    private JobRole jobRole; // Optional, only for STAFF
}
