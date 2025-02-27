package com.example.resturant_management_system.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.resturant_management_system.model.entities.WorkSchedule;
import com.example.resturant_management_system.security.UserPrincipal;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
	List<WorkSchedule> findByUser(UserPrincipal user);
	List<WorkSchedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
	List<WorkSchedule> findAll();
	List<WorkSchedule> findByUser_Id(long id);
	
	List<WorkSchedule> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
	boolean existsByUserIdAndDate(Long userId, LocalDate date);
	
	@Query("SELECT w FROM WorkSchedule w WHERE w.user.id = :userId AND (w.date > :today OR (w.date = :today AND w.endTime > :currentTime)) ORDER BY w.date ASC, w.startTime ASC")
	List<WorkSchedule> findNextUpcomingShifts(@Param("userId") Long userId, 
	                                          @Param("today") LocalDate today,
	                                          @Param("currentTime") LocalTime currentTime);


}
