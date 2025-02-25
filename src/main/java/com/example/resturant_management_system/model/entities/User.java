package com.example.resturant_management_system.model.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", columnDefinition= "VARCHAR(255) COLLATE utf8_general_ci")
	private String name;
	
	@Column(name = "email", columnDefinition = "VARCHAR(255) COLLATE utf8_general_ci", unique = true)
	private String email;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "hourlyWage", nullable = false)
    private Double hourlyWage = 0.0;
	
	@Column(name = "basicSalary", nullable = false)
    private Double basicSalary = 10.0; 
	
	@Column(name = "active", nullable = false)
	private boolean active = true;
	
	public Double getHourlyWage() {
	    return hourlyWage != null ? hourlyWage : 0.0; // Prevent null error
	}

	public void setHourlyWage(Double hourlyWage) {
	    this.hourlyWage = hourlyWage;
	}

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }
	
	@Enumerated(EnumType.STRING)
    private Role role; // ADMIN or STAFF
	
    @Enumerated(EnumType.STRING)
    private JobRole jobRole; // Specific job (only for STAFF)
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceRecords;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkSchedule> workSchedules;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaveRequest> leaveRequests;

    public List<Attendance> getAttendanceHistory() {
        return attendanceRecords;
    }

}