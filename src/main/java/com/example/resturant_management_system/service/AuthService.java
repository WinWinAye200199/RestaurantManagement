package com.example.resturant_management_system.service;

import com.example.resturant_management_system.model.request.LoginRequest;
import com.example.resturant_management_system.model.request.ResetPasswordRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.JwtResponse;
import com.example.resturant_management_system.security.UserPrincipal;

public interface AuthService {

	JwtResponse authenticate(LoginRequest loginRequest);

	ApiResponse changePassword(UserPrincipal currentUser, String oldPassword, String newPassword);

	ApiResponse requestResetPassword(String username);

	ApiResponse resetPassword(ResetPasswordRequest request);

}