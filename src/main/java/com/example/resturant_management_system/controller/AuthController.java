package com.example.resturant_management_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resturant_management_system.model.request.ChangePasswordRequest;
import com.example.resturant_management_system.model.request.LoginRequest;
import com.example.resturant_management_system.model.request.ResetPasswordRequest;
import com.example.resturant_management_system.model.request.ResetRequest;
import com.example.resturant_management_system.model.request.UserRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.JwtResponse;
import com.example.resturant_management_system.security.CurrentUser;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.AuthService;
import com.example.resturant_management_system.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

		
			JwtResponse jwtResponse = authService.authenticate(loginRequest);

			return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
		
	}

	@PostMapping("/signup")
	public ApiResponse signUp(@RequestBody UserRequest userRequest) {
		ApiResponse savedUser = userService.createUser(userRequest);
		
		return savedUser;
	}
	
	// Step 1: Request Password Reset
    @PostMapping("/request-reset-password")
    public ApiResponse requestResetPassword(@RequestBody ResetRequest request){
    	
        return authService.requestResetPassword(request.getEmail().trim());
    }

    // Step 2: Reset Password
    @PutMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }

	@PutMapping("/change-password")
	public ApiResponse changedPassword(@CurrentUser UserPrincipal currentUser,
			@RequestBody ChangePasswordRequest request) {

		String oldPassword = request.getOldPassword();
		String newPassword = request.getNewPassword();
		ApiResponse response = authService.changePassword(currentUser, oldPassword, newPassword);
		return response;
	}

}