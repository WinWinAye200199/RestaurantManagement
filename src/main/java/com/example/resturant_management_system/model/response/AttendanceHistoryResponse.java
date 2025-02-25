package com.example.resturant_management_system.model.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceHistoryResponse {
	private Long id;
	private String date;
	private String startTime;
	private String endTime;
	private double duration;
	private String status;
}
