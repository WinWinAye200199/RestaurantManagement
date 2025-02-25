package com.example.resturant_management_system.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReportResponse  {
    private Long userId;
    private String username;
    private String startDate;
    private String endDate;
    private double totalPayment;
    
    public PaymentReportResponse(Long userId, String username, String start, String end, double totalPayment) {
    	this.userId = userId;
        this.username = username;
        this.startDate = start;
        this.endDate = end;
        this.totalPayment = totalPayment;
    }
}
