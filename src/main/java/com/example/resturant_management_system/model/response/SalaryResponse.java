package com.example.resturant_management_system.model.response;

public class SalaryResponse {
	
    private double totalWorkedHours;
    private double totalSalary;

    // Constructor
    public SalaryResponse(double totalWorkedHours, double totalSalary) {
        this.totalWorkedHours = totalWorkedHours;
        this.totalSalary = totalSalary;
    }

    // Getters and Setters
    public double getTotalWorkedHours() {
        return totalWorkedHours;
    }

    public void setTotalWorkedHours(double totalWorkedHours) {
        this.totalWorkedHours = totalWorkedHours;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }
}
