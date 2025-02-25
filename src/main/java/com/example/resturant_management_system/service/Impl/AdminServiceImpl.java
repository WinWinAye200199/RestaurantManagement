package com.example.resturant_management_system.service.Impl;

import org.springframework.stereotype.Service;

import com.example.resturant_management_system.exception.BadRequestException;
import com.example.resturant_management_system.exception.NotFoundException;
import com.example.resturant_management_system.mapper.AttendanceMapper;
import com.example.resturant_management_system.mapper.LeaveRequestMapper;
import com.example.resturant_management_system.mapper.WorkScheduleMapper;
import com.example.resturant_management_system.model.entities.Attendance;
import com.example.resturant_management_system.model.entities.LeaveRequest;
import com.example.resturant_management_system.model.entities.LeaveStatus;
import com.example.resturant_management_system.model.entities.Role;
import com.example.resturant_management_system.model.entities.SalaryHistory;
import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.model.entities.WorkSchedule;
import com.example.resturant_management_system.model.dto.AttendanceDto;
import com.example.resturant_management_system.model.dto.LeaveRequestDto;
import com.example.resturant_management_system.model.dto.WorkScheduleDto;
import com.example.resturant_management_system.model.request.UpdateLeaveRequest;
import com.example.resturant_management_system.model.request.UpdateUserRequest;
import com.example.resturant_management_system.model.request.WorkScheduleRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.AttendanceResponse;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;
import com.example.resturant_management_system.model.response.PaymentReportResponse;
import com.example.resturant_management_system.model.response.StaffInfoResponse;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;
import com.example.resturant_management_system.repository.AttendanceRepository;
import com.example.resturant_management_system.repository.LeaveRequestRepository;
import com.example.resturant_management_system.repository.UserRepository;
import com.example.resturant_management_system.repository.WorkScheduleRepository;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.AdminService;
import com.example.resturant_management_system.service.EmailService;
import com.example.resturant_management_system.service.UserService;
import com.example.resturant_management_system.repository.SalaryHistoryRepository;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
    private final UserRepository userRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserService userService;
    private final WorkScheduleMapper workScheduleMapper;
    private final AttendanceMapper attendanceMapper;
    private final LeaveRequestMapper leaveRequestMapper;
    private final SalaryHistoryRepository salaryHistoryRepository;
    private final EmailService emailService;

    public List<StaffInfoResponse> getAllStaffInfo() {
        LocalDate today = LocalDate.now();

        return userRepository.findByRole(Role.USER).stream().map(user -> {
            StaffInfoResponse response = new StaffInfoResponse();
            response.setId(user.getId());
            response.setName(user.getName());
         // Set job role if the user is staff
            if (user.getRole() == Role.USER) {
                response.setJobRole(user.getJobRole() != null ? user.getJobRole().name() : "Not Assigned");
            } else {
                response.setJobRole("N/A");
            }
            response.setPhone(user.getPhone());
            response.setEmail(user.getEmail());
            response.setActive(user.isActive());
            response.setBasicSalary(user.getBasicSalary());

            // Get next upcoming shift
            workScheduleRepository.findNextUpcomingShift(user.getId(), today).ifPresent(schedule -> {
                String nextShift = String.format("%s, %s - %s", 
                        schedule.getDate(),
                        schedule.getStartTime(),
                        schedule.getEndTime());
                response.setNextShift(nextShift);
            });

            // Calculate attendance overview
            List<Attendance> attendanceList = attendanceRepository.findByUser_Id(user.getId());
            long totalWorkedHours = 0;
            for (Attendance attendance : attendanceList) {
                if (attendance.getClockIn() != null && attendance.getClockOut() != null) {
                    totalWorkedHours += Duration.between(attendance.getClockIn(), attendance.getClockOut()).toHours();
                }
            }
            response.setTotalHoursWorked(totalWorkedHours);

            return response;
        }).collect(Collectors.toList());
    }

	@Override
	public ApiResponse updateStaffInfo(long userId, UpdateUserRequest request) {
	
		userService.updateStaff(userId, request);
		
		return new ApiResponse(true," Staff information updated successfully!");
	}

	@Override
	public ApiResponse deleteStaff(long userId) {
		User foundUser = userRepository.getById(userId);
		
		foundUser.setActive(false);
		userRepository.save(foundUser);
		
		return new ApiResponse(true,"Successfully staff deleted!");
	}

	@Override
	public WorkScheduleResponse assignShift(WorkScheduleRequest request, UserPrincipal currentUser) {
		
		// Retrieve the user by userId (instead of name)
	    User user = userRepository.findByName(request.getName())
	            .orElseThrow(() -> new NotFoundException("User not found"));
	    
		// Check if the user already has a shift on the requested date
	    boolean shiftExists = workScheduleRepository.existsByUserIdAndDate(user.getId(), request.getDate());
	    if (shiftExists) {
	        throw new BadRequestException("User already has a shift on this date.");
	    }

	    // Create a new work schedule
	    WorkSchedule schedule = new WorkSchedule();
	    schedule.setDate(request.getDate());
	    schedule.setStartTime(request.getStartTime());
	    schedule.setEndTime(request.getEndTime());
	    
	    schedule.setUser(user);
		
		User foundManager = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new NotFoundException("Manager not found"));
		schedule.setManager(foundManager);
		
		WorkSchedule savedSchedule = workScheduleRepository.save(schedule);
		
		Attendance attendance = new Attendance();
		attendance.setClockIn(null);
		attendance.setClockOut(null);
		attendance.setDate(request.getDate());
		attendance.setTotalHours(0);
		attendance.setUser(user);
		attendance.setWorkSchedule(savedSchedule); // Link Attendance to WorkSchedule ✅

		attendanceRepository.save(attendance);
		savedSchedule.setAttendance(attendance); // Link WorkSchedule to Attendance ✅
		workScheduleRepository.save(savedSchedule); // Save the updated WorkSchedule ✅
     // Send an email to the assigned staff
        String emailSubject = "New Work Schedule Assigned";
        String emailBody = "Dear " + user.getName() + ",\n\n"
                + "You have been assigned a new work shift on " + savedSchedule.getDate() + ".\n"
                + "Time: " + savedSchedule.getStartTime() + " - " + savedSchedule.getEndTime() + "\n\n"
                + "Please check your schedule for more details.\n\n"
                + "Best Regards,\nManagement Team";

        emailService.sendEmail(user.getEmail(), emailSubject, emailBody);

        WorkScheduleDto dto = workScheduleMapper.maptoDto(schedule);
        WorkScheduleResponse response = workScheduleMapper.maptoResponse(dto);

        return response;
	}

	public WorkScheduleResponse updateShift(Long id, WorkScheduleRequest request, UserPrincipal currentUser) {
	  
		WorkSchedule schedule = workScheduleRepository.findById(id)
	            .orElseThrow(() -> new NotFoundException("Shift not found"));

	    schedule.setDate(request.getDate());
	    schedule.setStartTime(request.getStartTime());
	    schedule.setEndTime(request.getEndTime());

	    User foundManager = userRepository.findById(currentUser.getId())
	            .orElseThrow(() -> new NotFoundException("Manager not found"));
	    schedule.setManager(foundManager);

	    WorkSchedule updatedSchedule = workScheduleRepository.save(schedule);
	    
	    Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new NotFoundException("Attendance not found"));
	    
		attendance.setClockIn(null);
		attendance.setClockOut(null);
		attendance.setDate(request.getDate());
		attendance.setTotalHours(0);
		attendance.setUser(attendance.getUser());
		
		attendanceRepository.save(attendance);

	    // Send email notification to staff
	    String emailSubject = "Work Schedule Updated";
	    String emailBody = "Dear " + schedule.getUser().getName() + ",\n\n"
	            + "Your work schedule has been updated:\n"
	            + "New Date: " + updatedSchedule.getDate() + "\n"
	            + "Time: " + updatedSchedule.getStartTime() + " - " + updatedSchedule.getEndTime() + "\n\n"
	            + "Please check your schedule for the latest details.\n\n"
	            + "Best Regards,\nManagement Team";

	    emailService.sendEmail(schedule.getUser().getEmail(), emailSubject, emailBody);

	    return workScheduleMapper.maptoResponse(workScheduleMapper.maptoDto(updatedSchedule));
	}
	
	public void deleteShift(Long shiftId, UserPrincipal currentUser) {
	    WorkSchedule schedule = workScheduleRepository.findById(shiftId)
	            .orElseThrow(() -> new NotFoundException("Shift not found"));

//	    User foundManager = userRepository.findById(currentUser.getId())
//	            .orElseThrow(() -> new NotFoundException("Manager not found"));

	    workScheduleRepository.delete(schedule);

	    // Send email notification to staff
	    String emailSubject = "Work Schedule Cancelled";
	    String emailBody = "Dear " + schedule.getUser().getName() + ",\n\n"
	            + "Your scheduled shift on " + schedule.getDate() + " has been cancelled.\n"
	            + "Please contact your manager for more details.\n\n"
	            + "Best Regards,\nManagement Team";

	    emailService.sendEmail(schedule.getUser().getEmail(), emailSubject, emailBody);
	}


	
	@Override
	public List<WorkScheduleResponse> getAllSchedules() {
		List<WorkSchedule> schedules = workScheduleRepository.findAll();
		List<WorkScheduleDto> dtos = workScheduleMapper.maptoDto(schedules);
		List<WorkScheduleResponse> responses = workScheduleMapper.mapToResponse(dtos);
		return responses;
	}

	@Override
	public List<AttendanceResponse> getAllAttendances() {
		List<Attendance> attendances = attendanceRepository.findAll();
		List<AttendanceDto> dtos = attendanceMapper.maptoDto(attendances);
		List<AttendanceResponse> responses = attendanceMapper.maptoResponse(dtos);
		return responses;
	}

	@Override
	public List<LeaveRequestResponse> getAllLeaveRequests() {
		List<LeaveRequest> requests = leaveRequestRepository.findAll();
		List<LeaveRequestDto> dtos = leaveRequestMapper.maptoDto(requests);
		List<LeaveRequestResponse> responses = leaveRequestMapper.maptoResponse(dtos);
		return responses;
	}

	@Override
	public ApiResponse updateLeaveRequest(long userId, UpdateLeaveRequest request) {
		LeaveRequest leaveRequest = leaveRequestRepository.getById(userId);
		if(leaveRequest.getStatus().equals(LeaveStatus.PENDING)) {
			leaveRequest.setStatus(request.getStatus());
			leaveRequestRepository.save(leaveRequest);
		}
		User user = userRepository.getById(userId);
		// Send email notification to staff
	    String emailSubject = "Response Leave Request";
	    String emailBody = "Dear " + user.getName() + ",\n\n"
	            + "I am "+request.getStatus()+" your leave request on " + leaveRequest.getStartDate() + " .\n"
	            + "Please contact your manager for more details.\n\n"
	            + "Best Regards,\nManagement Team";

	    emailService.sendEmail(user.getEmail(), emailSubject, emailBody);
		return new ApiResponse(true,"Updated Leave Request!");
		
	}
	
//	 /**
//     * Calculate payment for a specific user over a period.
//     */
//    public double calculatePayment(Long userId, LocalDate start, LocalDate end) {
//        List<WorkSchedule> schedules = workScheduleRepository.findByUserIdAndDateBetween(userId, start, end);
//        
//        double totalHours = schedules.stream()
//            .mapToDouble(s -> Duration.between(s.getStartTime(), s.getEndTime()).toHours())
//            .sum();
//
//        User user = userRepository.findById(userId)
//            .orElseThrow(() -> new NotFoundException("User not found"));
//
//        return totalHours * user.getHourlyWage(); // Payment calculation
//    }
//	public double calculatePayment(Long userId, LocalDate start, LocalDate end) {
//	    // Get the user
//	    User user = userRepository.findById(userId)
//	        .orElseThrow(() -> new NotFoundException("User not found"));
//
//	    // Get actual worked hours from Attendance table
//	    Double totalHoursWorked = attendanceRepository.getTotalWorkedHours(userId, start, end);
//	    totalHoursWorked = (totalHoursWorked != null) ? totalHoursWorked : 0.0;
//
//	    // Calculate payment
//	    return totalHoursWorked * user.getHourlyWage();
//	}
	public double calculatePayment(Long userId, LocalDate start, LocalDate end) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new NotFoundException("User not found"));

	    // Ensure basicSalary is not null
	    double basicSalary = (user.getBasicSalary() != null) ? user.getBasicSalary() : 0.0;

	    // Get total worked hours from attendance
	    Double totalWorkedHours = attendanceRepository.getTotalWorkedHours(userId, start, end);
	    if (totalWorkedHours == null) {
	        totalWorkedHours = 0.0;
	    }

	    return basicSalary * totalWorkedHours;
//	    return basicSalary + (totalWorkedHours * user.getHourlyWage()); // Example: Fixed salary + overtime
	}


    /**
     * Generate payment report for a single user.
     */
    public PaymentReportResponse getPaymentReport(Long userId,String username, String start, String end) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
    	double totalPayment = calculatePayment(userId, startDate, endDate);
        return new PaymentReportResponse(userId, username, start, end, totalPayment);
    }

    /**
     * Generate payment report for all users.
     */
//    public List<PaymentReportResponse> generateOverallPaymentReport(String startDate, String endDate) {
////    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////    	LocalDate start = LocalDate.parse(startDate, formatter);
////        LocalDate end = LocalDate.parse(endDate, formatter);
//        List<User> users = userRepository.findAll();
//
//        return users.stream()
//            .map(user -> getPaymentReport(user.getId(), startDate, endDate))
//            .collect(Collectors.toList());
//    }
    public List<PaymentReportResponse> generateOverallPaymentReport(String startDate, String endDate) {
        // Convert String to LocalDate
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate start = LocalDate.parse(startDate, formatter);
//        LocalDate end = LocalDate.parse(endDate, formatter);


    	List<User> users = userRepository.findByRole(Role.USER);

        return users.stream()
            .map(user -> getPaymentReport(user.getId(),user.getName(), startDate, endDate)) // Pass LocalDate, not String
            .collect(Collectors.toList());
    }
    
    public User updateBasicSalary(Long userId, double newSalary, UserPrincipal currentUser) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        User manager = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new NotFoundException("Manager not found"));

        double oldSalary = user.getBasicSalary();
        user.setBasicSalary(newSalary);
        
        // Save updated salary
        userRepository.save(user);

        // Save salary change history
        SalaryHistory history = new SalaryHistory();
        history.setUser(user);
        history.setOldSalary(oldSalary);
        history.setNewSalary(newSalary);
        history.setChangeDate(LocalDate.now());
        history.setUpdatedBy(manager);
        salaryHistoryRepository.save(history);

        return user;
    }

}
