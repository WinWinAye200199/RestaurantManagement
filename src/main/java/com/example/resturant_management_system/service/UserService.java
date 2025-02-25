package com.example.resturant_management_system.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.model.request.RequestLeave;
import com.example.resturant_management_system.model.request.UpdateUserRequest;
import com.example.resturant_management_system.model.request.UserRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.AttendanceHistoryResponse;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;
import com.example.resturant_management_system.model.response.SalaryResponse;
import com.example.resturant_management_system.model.response.UserResponse;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;
import com.example.resturant_management_system.security.UserPrincipal;

public interface UserService {
	
	ApiResponse createUser(UserRequest userRequest); 
	List<User> getAllStaff();
	Optional<User> getStaffById(Long id);
	User updateStaff(Long id, UpdateUserRequest updatedStaff);
	void deleteStaff(Long id);
	UserResponse getUserProfile(UserPrincipal currentUser);
	ApiResponse createAttendance(long id,UserPrincipal currentUser);
	ApiResponse createClockOut(long workScheduleId, UserPrincipal currentUser);
	ApiResponse requestLeave(RequestLeave request, UserPrincipal currentUser);
	List<AttendanceHistoryResponse> getAttendanceHistory(UserPrincipal currentUser);
	List<WorkScheduleResponse> getWorkSchedule(UserPrincipal currentUser);
	List<LeaveRequestResponse> getLeaveHistory(UserPrincipal currentUser);
	SalaryResponse getSalaryDetails(Long userId, LocalDate startDate, LocalDate endDate);
//	void updateUserHourlyWage(User user);

}