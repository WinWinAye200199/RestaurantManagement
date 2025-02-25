package com.example.resturant_management_system.model.dto;

import com.example.resturant_management_system.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto {
	Long id;
	String message;
	boolean readStatus;
	User user;
}
