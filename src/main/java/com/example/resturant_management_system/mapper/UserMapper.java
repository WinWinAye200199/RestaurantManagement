package com.example.resturant_management_system.mapper;

import java.util.List;

import com.example.resturant_management_system.model.dto.UserDto;
import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.model.request.UserRequest;
import com.example.resturant_management_system.model.response.UserResponse;

public interface UserMapper {

	UserDto mapToDto(User user);
	List<UserDto> mapToDto(List<User> users);
	UserResponse mapToResponse(UserDto userDto);
	List<UserResponse> mapToResponse(List<UserDto> userDtos);
	UserDto mapToDto(UserRequest request);
}