package com.example.resturant_management_system.model.dto;

import java.util.List;

import com.example.resturant_management_system.model.entities.JobRole;
import com.example.resturant_management_system.model.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	
	private long id;
	private String name;
	private String email;
	private String phone;
	private Role role;
	private JobRole jobRole;
	private List<AttendanceDto> attendanceRecords;
	private List<WorkScheduleDto> workSchedules;
	private List<LeaveRequestDto> leaveRequests;
	private List<NotificationDto> notifications;

}