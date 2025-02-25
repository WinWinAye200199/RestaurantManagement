package com.example.resturant_management_system.model.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clock_in", nullable = true)
    private LocalTime clockIn;

    @Column(name = "clock_out", nullable = true)
    private LocalTime clockOut;
    
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key
    private User user;

    @Column(name = "total_hours")
    private double totalHours;
    
    @OneToOne
    @JoinColumn(name = "work_schedule_id")
    private WorkSchedule workSchedule;
    
}
