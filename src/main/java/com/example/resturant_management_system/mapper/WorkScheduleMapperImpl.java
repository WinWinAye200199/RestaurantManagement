package com.example.resturant_management_system.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.resturant_management_system.model.dto.WorkScheduleDto;
import com.example.resturant_management_system.model.entities.WorkSchedule;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;

@Component
public class WorkScheduleMapperImpl implements WorkScheduleMapper{

	@Override
	public List<WorkScheduleDto> maptoDto(List<WorkSchedule> schedules) {
		if(schedules == null) {
			return null;
		}
		List<WorkScheduleDto> dtos = new ArrayList<>();
		for(WorkSchedule schedule : schedules) {
			WorkScheduleDto dto = new WorkScheduleDto();
			dto.setId(schedule.getId());
			dto.setDate(schedule.getDate());
			dto.setStartTime(schedule.getStartTime());
			dto.setEndTime(schedule.getEndTime());
			dto.setUsername(schedule.getUser().getName());
			dto.setActive(schedule.getUser().isActive());
			dto.setFinish(schedule.isFinish());
			dto.setManagername(schedule.getManager().getName());
			dto.setAttendanceId(schedule.getAttendance().getId());
			System.out.println("in Mapper"+ schedule.getManager().getName());
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<WorkScheduleResponse> mapToResponse(List<WorkScheduleDto> scheduleDtos) {
		if(scheduleDtos == null) {
			return null;
		}
		List<WorkScheduleResponse> responses = new ArrayList<>();
		for(WorkScheduleDto scheduleDto : scheduleDtos) {
			WorkScheduleResponse response = new WorkScheduleResponse();
			response.setId(scheduleDto.getId());
			response.setDate(scheduleDto.getDate().toString());
			response.setStartTime(scheduleDto.getStartTime().toString());
			response.setEndTime(scheduleDto.getEndTime().toString());
			response.setStaffName(scheduleDto.getUsername());
			response.setActive(scheduleDto.isActive());
			response.setFinish(scheduleDto.isFinish());
			response.setManagerName(scheduleDto.getManagername());
			response.setAttendanceId(scheduleDto.getAttendanceId());
			responses.add(response);
		}
		return responses;
	}

	@Override
	public WorkScheduleDto maptoDto(WorkSchedule schedule) {
		if(schedule == null) {
			return null;
		}
		WorkScheduleDto dto = new WorkScheduleDto();
		dto.setId(schedule.getId());
		dto.setDate(schedule.getDate());
		dto.setStartTime(schedule.getStartTime());
		dto.setEndTime(schedule.getEndTime());
		dto.setUsername(schedule.getUser().getName());
		dto.setActive(schedule.getUser().isActive());
		dto.setManagername(schedule.getManager().getName());
		dto.setFinish(schedule.isFinish());
		dto.setAttendanceId(schedule.getAttendance().getId());
		
		return dto;
	}

	@Override
	public WorkScheduleResponse maptoResponse(WorkScheduleDto scheduleDto) {
		if(scheduleDto == null) {
			return null;
		}
		WorkScheduleResponse response = new WorkScheduleResponse();
		response.setId(scheduleDto.getId());
		response.setDate(scheduleDto.getDate().toString());
		response.setStaffName(scheduleDto.getUsername());
		response.setActive(scheduleDto.isActive());
		response.setStartTime(scheduleDto.getStartTime().toString());
		response.setEndTime(scheduleDto.getEndTime().toString());
		response.setManagerName(scheduleDto.getManagername());
		response.setFinish(scheduleDto.isFinish());
		response.setAttendanceId(scheduleDto.getAttendanceId());
			
		return response;
	}

}
