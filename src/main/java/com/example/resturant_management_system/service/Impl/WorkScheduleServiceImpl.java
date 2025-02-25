package com.example.resturant_management_system.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.resturant_management_system.model.dto.WorkScheduleDto;
import com.example.resturant_management_system.model.entities.WorkSchedule;
import com.example.resturant_management_system.repository.WorkScheduleRepository;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.WorkScheduleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkScheduleServiceImpl implements WorkScheduleService{

	
	private final WorkScheduleRepository workScheduleRepository;

	@Override
    // Method to get work schedules by user
    public List<WorkScheduleDto> getWorkSchedulesByUser(UserPrincipal user) {
        List<WorkSchedule> workSchedules = workScheduleRepository.findByUser(user);
        return workSchedules.stream()
                .map(workSchedule -> {
                    WorkScheduleDto workScheduleDto = new WorkScheduleDto();
                    workScheduleDto.setId(workSchedule.getId());
                    workScheduleDto.setStartTime(workSchedule.getStartTime());
                    workScheduleDto.setEndTime(workSchedule.getEndTime());
                    return workScheduleDto;
                })
                .collect(Collectors.toList());
    }
	
}
