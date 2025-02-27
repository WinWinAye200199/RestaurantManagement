package com.example.resturant_management_system.mapper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.example.resturant_management_system.model.dto.AttendanceDto;
import com.example.resturant_management_system.model.entities.Attendance;
import com.example.resturant_management_system.model.response.AttendanceHistoryResponse;
import com.example.resturant_management_system.model.response.AttendanceResponse;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;
import com.example.resturant_management_system.service.AdminService;
@Component
public class AttendanceMapperImpl implements AttendanceMapper{
	
	private final AdminService adminService;
	
	public AttendanceMapperImpl(@Lazy AdminService adminService) {
        this.adminService = adminService;
    }

	@Override
	public List<AttendanceHistoryResponse> mapToResponse(List<AttendanceDto> attendanceDtos) {
		if(attendanceDtos == null) {
			return null;
		}
		List<AttendanceHistoryResponse> responses = new ArrayList<>();
		List<WorkScheduleResponse> schedules = adminService.getAllSchedules();
		for(AttendanceDto attendanceDto:attendanceDtos) {
			AttendanceHistoryResponse response = new AttendanceHistoryResponse();
			response.setId(attendanceDto.getId());
			response.setDate(attendanceDto.getDate().toString());

			if (attendanceDto.getClockIn() == null) {
                response.setStatus("Absent");
	            response.setStartTime("Not Clocked In");
	            response.setEndTime("Not Clocked Out");
	        } else {
	            response.setStartTime(attendanceDto.getClockIn().toString());
	            response.setEndTime(Optional.ofNullable(attendanceDto.getClockOut())
	                                        .map(Object::toString)
	                                        .orElse("Not Clocked Out"));

	         // Determine if the employee was late
                response.setStatus(determineAttendanceStatus(attendanceDto.getClockIn(), schedules));
                response.setDuration(attendanceDto.getTotalHours());
	        }

			responses.add(response);
		}
		return responses;
	}

	@Override
	public List<AttendanceDto> maptoDto(List<Attendance> attendances) {
		if(attendances == null) {
			return null;
		}
		List<AttendanceDto> dtos = new ArrayList<>();
		for(Attendance attendance:attendances) {
			AttendanceDto dto = new AttendanceDto();
			dto.setId(attendance.getId());
			dto.setName(attendance.getUser().getName());
			dto.setDate(attendance.getDate());
			dto.setClockIn(attendance.getClockIn());
			dto.setClockOut(attendance.getClockOut());
			dto.setTotalHours(attendance.getTotalHours());

			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<AttendanceResponse> maptoResponse(List<AttendanceDto> attendanceDtos) {
		if(attendanceDtos == null) {
			return null;
		}
		List<AttendanceResponse> responses = new ArrayList<>();
		List<WorkScheduleResponse> schedules = adminService.getAllSchedules();
		for(AttendanceDto attendanceDto:attendanceDtos) {
			AttendanceResponse response = new AttendanceResponse();
			response.setId(attendanceDto.getId());
			response.setName(attendanceDto.getName());
			response.setDate(attendanceDto.getDate().toString());

			if (attendanceDto.getClockIn() == null) {
                response.setStatus("Absent");
                response.setStartTime("Not Clocked In");
                response.setEndTime("Not Clocked Out");
            } else {
                response.setStartTime(attendanceDto.getClockIn().toString());
                response.setEndTime(Optional.ofNullable(attendanceDto.getClockOut())
                                            .map(Object::toString)
                                            .orElse("Not Clocked Out"));
                
                response.setStatus(determineAttendanceStatus(attendanceDto.getClockIn(), schedules));
                response.setDuration(attendanceDto.getTotalHours());
            }

	        responses.add(response);
		}
		return responses;
	}

	/**
     * Helper method to determine if an employee was late or present.
     */
	private String determineAttendanceStatus(LocalTime clockInTime, List<WorkScheduleResponse> schedules) {
        if (clockInTime == null) {
            return "Absent";
        }

        if (schedules == null || schedules.isEmpty()) {
            return "Unknown"; // No schedule data available
        }

        for (WorkScheduleResponse schedule : schedules) {
            if (schedule.getStartTime() != null) {
                LocalTime shiftStartTime = LocalTime.parse(schedule.getStartTime());

                if (clockInTime.isAfter(shiftStartTime.plusMinutes(5))) { // 5-minute grace period
                    return "Late";
                } else {
                    return "Present";
                }
            }
        }

        return "Absent"; // Default if no schedule matches
    }
}
