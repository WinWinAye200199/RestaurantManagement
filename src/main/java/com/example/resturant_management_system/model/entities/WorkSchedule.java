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
@Getter
@Setter
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    
    @Column(name = "finish", nullable = false)
    private boolean finish=false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Assigned staff member

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager; // Manager who created the schedule
    
    // Getters and Setters
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @OneToOne(mappedBy = "workSchedule")
    private Attendance attendance;
}
