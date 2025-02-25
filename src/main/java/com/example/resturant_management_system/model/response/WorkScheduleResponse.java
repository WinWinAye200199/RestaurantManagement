package com.example.resturant_management_system.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkScheduleResponse {
	private long id;
    private String date;
    private String startTime;
    private String endTime;
    private String staffName;
    private boolean active;
    private String managerName; 
    private long attendanceId;
    private boolean finish;
}
