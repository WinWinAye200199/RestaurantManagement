package com.example.resturant_management_system.model.dto;

import java.time.LocalDate;

import com.example.resturant_management_system.model.entities.LeaveStatus;
import com.example.resturant_management_system.model.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveRequestDto {
	private Long id;
	private String reason;
	private LocalDate startDate;
	private LocalDate endDate;
	private LeaveStatus status;
	private User user;
}
