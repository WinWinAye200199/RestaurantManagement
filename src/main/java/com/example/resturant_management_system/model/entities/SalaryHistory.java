package com.example.resturant_management_system.model.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SalaryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User user; // Link salary change to user

    private double oldSalary;
    private double newSalary;
    private LocalDate changeDate;

    @ManyToOne
    private User updatedBy; // Manager who made the change

}
