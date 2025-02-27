package com.example.resturant_management_system.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceDto {

    private Long id;
    private String name;
    private LocalDate date;
    private LocalTime clockIn;
    private LocalTime clockOut;
    private double totalHours;

}