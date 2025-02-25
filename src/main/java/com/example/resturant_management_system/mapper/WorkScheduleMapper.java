package com.example.resturant_management_system.mapper;

import java.util.List;

import com.example.resturant_management_system.model.dto.WorkScheduleDto;
import com.example.resturant_management_system.model.entities.WorkSchedule;
import com.example.resturant_management_system.model.response.WorkScheduleResponse;

public interface WorkScheduleMapper {
	List<WorkScheduleDto> maptoDto(List<WorkSchedule> schedules);
	List<WorkScheduleResponse> mapToResponse(List<WorkScheduleDto> scheduleDtos);
	WorkScheduleDto maptoDto(WorkSchedule schedule);
	WorkScheduleResponse maptoResponse(WorkScheduleDto scheduleDto);

}
