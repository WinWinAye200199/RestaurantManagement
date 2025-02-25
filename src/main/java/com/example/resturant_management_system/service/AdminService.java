package com.example.resturant_management_system.service;

import java.time.LocalDate;
import java.util.List;

import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.model.request.UpdateLeaveRequest;
import com.example.resturant_management_system.model.request.UpdateUserRequest;
import com.example.resturant_management_system.model.request.WorkScheduleRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.AttendanceResponse;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;
import com.example.resturant_management_system.model.response.PaymentReportResponse;
import com.example.resturant_management_system.model.response.StaffInfoResponse;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;
import com.example.resturant_management_system.security.UserPrincipal;

public interface AdminService {
	List<StaffInfoResponse> getAllStaffInfo();
	ApiResponse updateStaffInfo(long userId, UpdateUserRequest request);
	ApiResponse deleteStaff(long userId);
	WorkScheduleResponse assignShift(WorkScheduleRequest request, UserPrincipal currentUser);
	List<WorkScheduleResponse> getAllSchedules();
	List<AttendanceResponse> getAllAttendances();
	List<LeaveRequestResponse> getAllLeaveRequests();
	ApiResponse updateLeaveRequest(long userId, UpdateLeaveRequest request);
	double calculatePayment(Long userId, LocalDate startDate, LocalDate endDate);
	PaymentReportResponse getPaymentReport(Long userId, String username, String startDate, String endDate);
	List<PaymentReportResponse> generateOverallPaymentReport(String startDate, String endDate);
	User updateBasicSalary(Long userId, double newSalary, UserPrincipal currentUser);
	WorkScheduleResponse updateShift(Long shiftId, WorkScheduleRequest request, UserPrincipal currentUser);
	void deleteShift(Long shiftId, UserPrincipal currentUser);
}
