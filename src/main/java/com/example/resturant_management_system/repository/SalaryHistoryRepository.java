package com.example.resturant_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.resturant_management_system.model.entities.SalaryHistory;

public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long>{

}
