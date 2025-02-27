package com.example.resturant_management_system.controller;

import org.springframework.web.bind.annotation.*;

import com.example.resturant_management_system.model.request.RequestLeave;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.UserResponse;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;
import com.example.resturant_management_system.security.CurrentUser;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.UserService;
import com.example.resturant_management_system.model.response.AttendanceHistoryResponse;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;
import com.example.resturant_management_system.model.response.SalaryResponse;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	final private UserService userService;
	
	@GetMapping("/profile")
	public UserResponse getUserProfile(@CurrentUser UserPrincipal currentUser) {
		UserResponse response = userService.getUserProfile(currentUser);
		return response;
	}
	
	@PostMapping("/attendance/clock-in/{id}")
	public ApiResponse clockIn(@PathVariable("id") long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse response = userService.createAttendance(id, currentUser);
		return response;
	}

	@PostMapping("/attendance/clock-out/{id}")
	public ApiResponse clockOut(@PathVariable("id") long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse response = userService.createClockOut(id, currentUser);
		return response;
	}
	
	@PostMapping("/leave/request")
	public ApiResponse requestLeave(@RequestBody RequestLeave request, @CurrentUser UserPrincipal currentUser) {
		ApiResponse response = userService.requestLeave(request, currentUser);
		return response;
	}
	
	@GetMapping("/leave/history")
	public List<LeaveRequestResponse> leaveHistory(@CurrentUser UserPrincipal currentUser) {
		List<LeaveRequestResponse> response = userService.getLeaveHistory(currentUser);
		return response;
	}

	@GetMapping("/attendance/history")
	public List<AttendanceHistoryResponse> getAttendanceHistory(@CurrentUser UserPrincipal currentUser) {
		List<AttendanceHistoryResponse> history = userService.getAttendanceHistory(currentUser);
		return history;
	}
	
	@GetMapping("/work-schedule")
	public List<WorkScheduleResponse> getWorkSchedule(@CurrentUser UserPrincipal currentUser){
		List<WorkScheduleResponse> response = userService.getWorkSchedule(currentUser);
		return response;
	}
	
	@GetMapping("/salary")
    public SalaryResponse getSalary(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        // Convert date parameters if provided
        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

        // Fetch salary details
        return userService.getSalaryDetails(currentUser.getId(), start, end);
    }

}
