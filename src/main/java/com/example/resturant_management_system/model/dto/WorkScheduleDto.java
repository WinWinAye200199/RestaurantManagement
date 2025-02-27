package com.example.resturant_management_system.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkScheduleDto {
	private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean active;
    private String username;
    private String managername; 
    private long attendanceId;
    private boolean finish;
   
}
