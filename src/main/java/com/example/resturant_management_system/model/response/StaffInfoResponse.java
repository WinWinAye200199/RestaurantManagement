package com.example.resturant_management_system.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffInfoResponse {
	private long id;
	private String name;
    private String jobRole;
    private String phone;
    private String email;
    private boolean active;
    private Double basicSalary;
    private String nextShift; // "2025-01-26, 09:00 AM - 05:00 PM"
    private String attendanceOverview; // "85% Present"
    private long totalHoursWorked; // Total hours
}
