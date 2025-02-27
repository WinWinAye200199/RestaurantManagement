package com.example.resturant_management_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.resturant_management_system.model.request.UpdateUserRequest;
import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.model.request.BasicSalaryRequest;
import com.example.resturant_management_system.model.request.UpdateLeaveRequest;
import com.example.resturant_management_system.model.request.WorkScheduleRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.AttendanceResponse;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;
import com.example.resturant_management_system.model.response.PaymentReportResponse;
import com.example.resturant_management_system.model.response.StaffInfoResponse;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;
import com.example.resturant_management_system.repository.UserRepository;
import com.example.resturant_management_system.security.CurrentUser;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.AdminService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	private final AdminService adminService;
	private final UserRepository userRepository;
	
	@GetMapping("/profiles")
	public List<StaffInfoResponse> getUserProfiles() {
		List<StaffInfoResponse> response = adminService.getAllStaffInfo();
		return response;
	}
	
	@PutMapping("/{userId}")
	public ApiResponse updateStaff(@PathVariable("userId") long userId,@RequestBody UpdateUserRequest request) {
		ApiResponse response = adminService.updateStaffInfo(userId, request);
		return response;
	}
	
	@DeleteMapping("/removeStaff/{userId}")
	public ApiResponse deleteStaff(@PathVariable("userId") long userId) {
		ApiResponse response = adminService.deleteStaff(userId);
		return response;
	}
	
	@PostMapping("/assignShift")
	public WorkScheduleResponse assignShift(@RequestBody WorkScheduleRequest request,
			@CurrentUser UserPrincipal currentUser) {
		WorkScheduleResponse response = adminService.assignShift(request, currentUser);
		return response;
	}
	
	@GetMapping("/getAllShift")
	public List<WorkScheduleResponse> getAllSchedules() {
		List<WorkScheduleResponse> responses = adminService.getAllSchedules();
		return responses;
	}
	
	@PutMapping("/shifts/{id}")
    public ResponseEntity<WorkScheduleResponse> updateShift(
    		@PathVariable Long id,
            @RequestBody WorkScheduleRequest request,
            @CurrentUser UserPrincipal currentUser) {
        
        WorkScheduleResponse response = adminService.updateShift(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/removeShift/{shiftId}")
    public ResponseEntity<String> deleteShift(
            @PathVariable Long shiftId,
            @CurrentUser UserPrincipal currentUser) {
        
        adminService.deleteShift(shiftId, currentUser);
        return ResponseEntity.ok("Shift deleted successfully.");
    }
	
	@GetMapping("/getAllAttendance")
	public List<AttendanceResponse> getAllAttendances(){
		List<AttendanceResponse> responses = adminService.getAllAttendances();
		return responses;
	}
	
	@GetMapping("/getAllLeaveRequest")
	public List<LeaveRequestResponse> getAllLeaveRequest(){
		List<LeaveRequestResponse> responses = adminService.getAllLeaveRequests();
		return responses;
	}
	
	@PutMapping("/updateLeaveRequest/{id}")
	public ApiResponse updateLeaveRequest(@PathVariable("id") long userId, @RequestBody UpdateLeaveRequest status) {
		ApiResponse response = adminService.updateLeaveRequest(userId, status);
		return response;
	}
	@GetMapping("/{userId}")
    public ResponseEntity<PaymentReportResponse> getPaymentReport(
            @PathVariable Long userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
		User foundUser = userRepository.getById(userId);
        return ResponseEntity.ok(adminService.getPaymentReport(userId,foundUser.getName(), startDate, endDate));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentReportResponse>> getOverallPayments(
    		@RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(adminService.generateOverallPaymentReport(startDate, endDate));
    }
    
    @PutMapping("/users/{userId}/salary")
    public ResponseEntity<String> updateBasicSalary(
        @PathVariable Long userId, 
        @RequestBody BasicSalaryRequest request,@CurrentUser UserPrincipal currentUser) { // Manager ID to track who updated it
    	
        User updatedUser = adminService.updateBasicSalary(userId, request.getNewSalary(),currentUser);
        return ResponseEntity.ok("Salary updated to: " + updatedUser.getBasicSalary());
    }

}
