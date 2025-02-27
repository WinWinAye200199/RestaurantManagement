package com.example.resturant_management_system.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceResponse {
	private long id;
	private String name;
	private String date;
	private String startTime;
	private String endTime;
	private double duration;
	private String status;
}
