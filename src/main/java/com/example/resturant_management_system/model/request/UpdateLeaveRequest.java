package com.example.resturant_management_system.model.request;

import com.example.resturant_management_system.model.entities.LeaveStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLeaveRequest {
	private LeaveStatus status;
}
