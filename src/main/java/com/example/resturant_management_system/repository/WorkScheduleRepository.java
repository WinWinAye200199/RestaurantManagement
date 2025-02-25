package com.example.resturant_management_system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.resturant_management_system.model.entities.WorkSchedule;
import com.example.resturant_management_system.security.UserPrincipal;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
	List<WorkSchedule> findByUser(UserPrincipal user);
	List<WorkSchedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
	List<WorkSchedule> findAll();
	List<WorkSchedule> findByUser_Id(long id);
	
	@Query("SELECT ws FROM WorkSchedule ws WHERE ws.user.id = :userId AND ws.date >= :today ORDER BY ws.date ASC, ws.startTime ASC")
	Optional<WorkSchedule> findNextUpcomingShift(Long userId, LocalDate today);
	List<WorkSchedule> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
	boolean existsByUserIdAndDate(Long userId, LocalDate date);

}
