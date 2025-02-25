package com.example.resturant_management_system.model.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotBlank
public class RequestLeave {
	private String reason;
	private LocalDate startDate;
	private LocalDate endDate;
}
