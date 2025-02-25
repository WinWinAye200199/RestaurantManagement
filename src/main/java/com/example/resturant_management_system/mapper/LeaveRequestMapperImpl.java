package com.example.resturant_management_system.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.resturant_management_system.model.dto.LeaveRequestDto;
import com.example.resturant_management_system.model.entities.LeaveRequest;
import com.example.resturant_management_system.model.response.LeaveRequestResponse;

@Component
public class LeaveRequestMapperImpl implements LeaveRequestMapper{

	@Override
	public List<LeaveRequestDto> maptoDto(List<LeaveRequest> requests) {
		if(requests == null) {
			return null;
		}
		List<LeaveRequestDto> dtos = new ArrayList<>();
		for(LeaveRequest request: requests) {
			LeaveRequestDto dto = new LeaveRequestDto();
			dto.setId(request.getId());
			dto.setUser(request.getUser());
			dto.setStartDate(request.getStartDate());
			dto.setEndDate(request.getEndDate());
			dto.setReason(request.getReason());
			dto.setStatus(request.getStatus());
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<LeaveRequestResponse> maptoResponse(List<LeaveRequestDto> dtos) {
		if(dtos == null) {
			return null;
		}
		List<LeaveRequestResponse> responses = new ArrayList<>();
		for(LeaveRequestDto dto: dtos) {
			LeaveRequestResponse response = new LeaveRequestResponse();
			response.setId(dto.getId());
			response.setName(dto.getUser().getName());
			response.setStartDate(dto.getStartDate().toString());
			response.setEndDate(dto.getEndDate().toString());
			response.setReason(dto.getReason());
			response.setStatus(dto.getStatus());
			responses.add(response);
		}
		return responses;
	}

}
