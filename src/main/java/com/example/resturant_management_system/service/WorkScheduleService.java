package com.example.resturant_management_system.service;

import java.util.List;

import com.example.resturant_management_system.model.dto.WorkScheduleDto;
import com.example.resturant_management_system.security.UserPrincipal;

public interface WorkScheduleService {
	List<WorkScheduleDto> getWorkSchedulesByUser(UserPrincipal currentUser);
	
}
