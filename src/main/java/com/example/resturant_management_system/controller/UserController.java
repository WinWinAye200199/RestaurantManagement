package com.example.resturant_management_system.controller;

import org.springframework.web.bind.annotation.*;

import com.example.resturant_management_system.model.request.PaymentReportRequest;
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
	

//    @GetMapping("/profile")
//    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal User currentUser) {
//        return ResponseEntity.ok(currentUser);
//    }
//	@GetMapping("/profile")
//	public ResponseEntity<UserDto> getUserProfile(@CurrentUser UserPrincipal currentUser) {
//	    // Map User entity to UserDto
//	    UserDto userDto = new UserDto();
//	    userDto.setId(currentUser.getId());
//	    userDto.setName(currentUser.getUsername());
//	    userDto.setEmail(currentUser.getEmail());
//	    userDto.setPhone(currentUser.getPhone());
//	    
//	    // Assuming you have services to map the lists of related entities
//	    userDto.setRoles(roleService.getRolesByUser(currentUser));  // Map roles to RoleDto
//	    userDto.setAttendanceRecords(attendanceService.getAttendanceRecordsByUser(currentUser));  // Map attendance to AttendanceDto
//	    userDto.setWorkSchedules(workScheduleService.getWorkSchedulesByUser(currentUser));  // Map work schedules to WorkScheduleDto
//	    userDto.setLeaveRequests(leaveRequestService.getLeaveRequestsByUser(currentUser));  // Map leave requests to LeaveRequestDto
//	    userDto.setNotifications(notificationService.getNotificationsByUser(currentUser));  // Map notifications to NotificationDto
//
//	    return ResponseEntity.ok(userDto);
//	}
	
	@GetMapping("/profile")
	public UserResponse getUserProfile(@CurrentUser UserPrincipal currentUser) {
		UserResponse response = userService.getUserProfile(currentUser);
		return response;
	}
	
//    @PostMapping("/attendance/clock-in")
//    public ResponseEntity<?> clockIn(@AuthenticationPrincipal User currentUser) {
//        Attendance attendance = new Attendance();
//        attendance.setClockIn(LocalDateTime.now());
//        attendance.setUser(currentUser);
//        attendanceService.save(attendance);
//        return ResponseEntity.ok("Clocked in successfully.");
//    }
	@PostMapping("/attendance/clock-in/{id}")
	public ApiResponse clockIn(@PathVariable("id") long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse response = userService.createAttendance(id, currentUser);
		return response;
	}

//    @PostMapping("/attendance/clock-out")
//    public ResponseEntity<?> clockOut(@AuthenticationPrincipal User currentUser) {
//        Attendance attendance = attendanceService.findLastAttendance(currentUser);
//        if (attendance.getClockOut() != null) {
//            return ResponseEntity.badRequest().body("Already clocked out.");
//        }
//        attendance.setClockOut(LocalDateTime.now());
//        attendance.setTotalHours(Duration.between(attendance.getClockIn(), attendance.getClockOut()).toHours());
//        attendanceService.save(attendance);
//        return ResponseEntity.ok("Clocked out successfully.");
//    }
	
	@PostMapping("/attendance/clock-out/{id}")
	public ApiResponse clockOut(@PathVariable("id") long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse response = userService.createClockOut(id, currentUser);
		return response;
	}
    
    

//    @PostMapping("/leave/request")
//    public ResponseEntity<?> requestLeave(@RequestBody RequestLeave request, @AuthenticationPrincipal User currentUser) {
//        request.setStatus(LeaveStatus.PENDING);
//        request.setUser(currentUser);
//        leaveRequestService.save(request);
//        return ResponseEntity.ok("Leave request submitted.");
//    }
	
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

//    @GetMapping("/attendance/history")
//    public ResponseEntity<?> getAttendanceHistory(@CurrentUser UserPrincipal currentUser) {
//        List<Attendance> attendanceHistory = attendanceService.findByUser(currentUser);
//        return ResponseEntity.ok(attendanceHistory);
//    }
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
