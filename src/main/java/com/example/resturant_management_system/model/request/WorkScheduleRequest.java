package com.example.resturant_management_system.model.request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkScheduleRequest {
	private String name;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
}
