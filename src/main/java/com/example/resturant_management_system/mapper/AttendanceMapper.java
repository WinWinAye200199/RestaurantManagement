package com.example.resturant_management_system.mapper;

import java.util.List;

import com.example.resturant_management_system.model.dto.AttendanceDto;
import com.example.resturant_management_system.model.entities.Attendance;
import com.example.resturant_management_system.model.response.AttendanceHistoryResponse;
import com.example.resturant_management_system.model.response.AttendanceResponse;

public interface AttendanceMapper {
	List<AttendanceDto> maptoDto(List<Attendance> attendances);
	List<AttendanceHistoryResponse> mapToResponse(List<AttendanceDto> attendanceDtos);
	List<AttendanceResponse> maptoResponse(List<AttendanceDto> attendanceDtos);
}
