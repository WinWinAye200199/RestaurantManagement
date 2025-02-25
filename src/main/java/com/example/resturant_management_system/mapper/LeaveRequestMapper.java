package com.example.resturant_management_system.mapper;

import java.util.List;

import com.example.resturant_management_system.model.dto.LeaveRequestDto;
import com.example.resturant_management_system.model.entities.LeaveRequest;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;

public interface LeaveRequestMapper {
	List<LeaveRequestDto> maptoDto(List<LeaveRequest> requests);
	List<LeaveRequestResponse> maptoResponse(List<LeaveRequestDto> dtos);
}
