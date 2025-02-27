package com.example.resturant_management_system.service.Impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.resturant_management_system.exception.BadRequestException;
import com.example.resturant_management_system.exception.NotFoundException;
import com.example.resturant_management_system.mapper.AttendanceMapper;
import com.example.resturant_management_system.mapper.LeaveRequestMapper;
import com.example.resturant_management_system.mapper.UserMapper;
import com.example.resturant_management_system.mapper.WorkScheduleMapper;
import com.example.resturant_management_system.model.dto.AttendanceDto;
import com.example.resturant_management_system.model.dto.LeaveRequestDto;
import com.example.resturant_management_system.model.dto.UserDto;
import com.example.resturant_management_system.model.dto.WorkScheduleDto;
import com.example.resturant_management_system.model.entities.Attendance;
import com.example.resturant_management_system.model.entities.LeaveRequest;
import com.example.resturant_management_system.model.entities.LeaveStatus;
import com.example.resturant_management_system.model.entities.Role;
import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.model.entities.WorkSchedule;
import com.example.resturant_management_system.model.request.RequestLeave;
import com.example.resturant_management_system.model.request.UpdateUserRequest;
import com.example.resturant_management_system.model.request.UserRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.AttendanceHistoryResponse;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;
import com.example.resturant_management_system.model.response.SalaryResponse;
import com.example.resturant_management_system.model.response.UserResponse;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;
import com.example.resturant_management_system.repository.AttendanceRepository;
import com.example.resturant_management_system.repository.LeaveRequestRepository;
import com.example.resturant_management_system.repository.UserRepository;
import com.example.resturant_management_system.repository.WorkScheduleRepository;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.AttendanceService;
import com.example.resturant_management_system.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceService attendanceService;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final AttendanceMapper attendanceMapper;
    private final WorkScheduleRepository workScheduleRepository;
    private final WorkScheduleMapper workScheduleMapper;

    @Override
    public ApiResponse createUser(UserRequest userRequest) {
        // Validate password and confirmed password
        if (!userRequest.getPassword().equals(userRequest.getConfirmedPassword())) {
            throw new BadRequestException("Password and confirmed password do not match!");
        }

        // Create a new user entity
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        user.setRole(userRequest.getRole());
        user.setJobRole(userRequest.getRole() == Role.USER ? userRequest.getJobRole() : null);


        // Save the user
        userRepository.save(user);

        return new ApiResponse(true, "Sign up Successfully!");
    }
    
    public List<User> getAllStaff() {
        return userRepository.findAll();
    }
    
    public Optional<User> getStaffById(Long id) {
        return userRepository.findById(id);
    }
    
    public User updateStaff(Long id, UpdateUserRequest updatedStaff) {
        return userRepository.findById(id).map(user -> {
        	System.out.println("Name"+updatedStaff.getName());
            user.setName(updatedStaff.getName());
        	user.setEmail(updatedStaff.getEmail());
            user.setPhone(updatedStaff.getPhone());
            user.setRole(updatedStaff.getRole());
            return userRepository.save(user);
        }).orElseThrow(() -> new NotFoundException("Staff not found with id " + id));
    }

    public void deleteStaff(Long id) {
        userRepository.deleteById(id);
    }

	@Override
	public UserResponse getUserProfile(UserPrincipal currentUser) {
		User foundUser = userRepository.findByName(currentUser.getName()).orElseThrow(
				() -> new BadRequestException("Username Not Found : username -> " + currentUser.getUsername()));
		if( foundUser == null) {
			throw new NotFoundException("User is not existed!");
		}
		UserDto userDto = userMapper.mapToDto(foundUser);
		UserResponse response = userMapper.mapToResponse(userDto);
		return response;
	}

	@Override
	public ApiResponse createAttendance(long id, UserPrincipal currentUser) {
	    // Find the work schedule by ID
	    WorkSchedule workSchedule = workScheduleRepository.findById(id)
	            .orElseThrow(() -> new BadRequestException("WorkSchedule Not Found!"));

	    
	    

	        // Allow clock-in
	        Attendance attendance = attendanceRepository.findById(id)
	        		.orElseThrow(() -> new BadRequestException("Attendance Not Found!"));
	        attendance.setClockIn(LocalTime.now());
	        attendanceRepository.save(attendance);

	        if (!workSchedule.isFinish()) {
		        workSchedule.setFinish(true);
		        workScheduleRepository.save(workSchedule);
		        }
	        return new ApiResponse(true, "Clock in successfully!");
	}
	@Override
	public ApiResponse createClockOut(long workScheduleId, UserPrincipal currentUser) {
	    // Find the user
	    User foundUser = userRepository.findByName(currentUser.getName())
	            .orElseThrow(() -> new BadRequestException("Username Not Found: " + currentUser.getUsername()));

	    // Find the work schedule by ID
	    WorkSchedule workSchedule = workScheduleRepository.findById(workScheduleId)
	            .orElseThrow(() -> new BadRequestException("WorkSchedule Not Found!"));

	    // Check if today matches the scheduled date
	    if (!workSchedule.getDate().equals(LocalDate.now())) {
	        return new ApiResponse(false, "You can only clock out on your scheduled workday.");
	    }

	    // Find the latest clock-in record for today
	    Attendance attendance = attendanceService.findLastAttendance(foundUser);

	    if (attendance.getClockOut() != null) {
	        return new ApiResponse(false, "Already clocked out.");
	    }

	    // Set clock-out time and calculate total hours worked
	    attendance.setClockOut(LocalTime.now());
	    long minutesWorked = Duration.between(attendance.getClockIn(), attendance.getClockOut()).toMinutes();
	    double totalHoursWorked = minutesWorked / 60.0; // Convert minutes to decimal hours
	    attendance.setTotalHours(totalHoursWorked);

	    attendanceService.save(attendance); // Save updated attendance

	    // Update user's hourly wage based on total hours worked
	    updateUserHourlyWage(attendance.getUser());

	    return new ApiResponse(true, "Clock out successfully!");
	}

	private void updateUserHourlyWage(User user) {
	    List<Attendance> attendances = attendanceRepository.findByUser_Id(user.getId());

	    // Calculate total worked hours
	    double totalWorkedHours = attendances.stream()
	        .mapToDouble(Attendance::getTotalHours)
	        .sum();

	    if (user.getBasicSalary() != null && totalWorkedHours > 0) {
	        double newHourlyWage = user.getBasicSalary() / totalWorkedHours;
	        user.setHourlyWage(newHourlyWage);
	        userRepository.save(user); // Update user record
	    }
	}

	@Override
	public ApiResponse requestLeave(RequestLeave request, UserPrincipal currentUser) {
		User foundUser = userRepository.findByName(currentUser.getName()).orElseThrow(
				() -> new BadRequestException("Username Not Found : username -> " + currentUser.getUsername()));
		if( foundUser == null) {
			throw new NotFoundException("User is not existed!");
		}
		LeaveRequest userRequest = new LeaveRequest();
		userRequest.setStatus(LeaveStatus.PENDING);
		userRequest.setUser(foundUser);
		userRequest.setReason(request.getReason());
		userRequest.setStartDate(request.getStartDate());
		userRequest.setEndDate(request.getEndDate());
		leaveRequestRepository.save(userRequest);
		
		return new ApiResponse(true,"Leave request submitted.");
	}

	@Override
	public List<AttendanceHistoryResponse> getAttendanceHistory(UserPrincipal currentUser) {
		List<Attendance> attendanceHistory = attendanceRepository.findByUser_Id(currentUser.getId());
		List<AttendanceDto> dtos = attendanceMapper.maptoDto(attendanceHistory);
		List<AttendanceHistoryResponse> responses = attendanceMapper.mapToResponse(dtos);
		return responses;
	}

	@Override
	public List<WorkScheduleResponse> getWorkSchedule(UserPrincipal currentUser) {
		List<WorkSchedule> schedules = workScheduleRepository.findByUser_Id(currentUser.getId());
		for(WorkSchedule schedule: schedules) {
			System.out.print(schedule.getManager().getName());
		}
		List<WorkScheduleDto> dtos = workScheduleMapper.maptoDto(schedules);
		List<WorkScheduleResponse> responses = workScheduleMapper.mapToResponse(dtos);
		return responses;
	}

	@Override
	public List<LeaveRequestResponse> getLeaveHistory(UserPrincipal currentUser) {
		List<LeaveRequest> requests = leaveRequestRepository.findAllByUser_Id(currentUser.getId());
		List<LeaveRequestDto> dtos = leaveRequestMapper.maptoDto(requests);
		List<LeaveRequestResponse> responses = leaveRequestMapper.maptoResponse(dtos);
		return responses;
	}

	@Override
	public SalaryResponse getSalaryDetails(Long userId, LocalDate startDate, LocalDate endDate) {
        // Fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        // If no date range is provided, default to the current month
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);  // First day of the current month
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // Last day of the month
        }

        // Fetch attendance records within the date range
        List<Attendance> attendanceRecords = attendanceRepository.findByUser_IdAndDateBetween(userId, startDate, endDate);

        // Calculate total worked hours
        double totalWorkedHours = attendanceRecords.stream()
                .mapToDouble(Attendance::getTotalHours)
                .sum();

        // Calculate total salary
        double totalSalary = totalWorkedHours * user.getBasicSalary();

        return new SalaryResponse(totalWorkedHours, totalSalary);
    }
}
