package com.example.resturant_management_system.model.response;


import com.example.resturant_management_system.model.entities.LeaveStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveRequestResponse {
	private long id;
	private String name;
	private String startDate;
	private String endDate;
	private String reason;
	private LeaveStatus Status;
	
}
