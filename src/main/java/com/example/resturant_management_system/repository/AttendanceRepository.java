package com.example.resturant_management_system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.resturant_management_system.model.entities.Attendance;
import com.example.resturant_management_system.model.entities.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	Optional<Attendance> findTopByUser_IdOrderByClockInDesc(Long userId);
	List<Attendance> findByUser_Id(Long userId);
	
	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.user.id = :userId")
    long countTotalPresentDays(Long userId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user.id = :userId")
    long countTotalDays(Long userId);
    
    @Query("SELECT SUM(a.totalHours) FROM Attendance a WHERE a.user.id = :userId")
    long calculateTotalHoursWorked(Long userId);
    
    List<Attendance> findAll();
    
    @Query(value = "SELECT SUM(TIMESTAMPDIFF(HOUR, a.clock_in, a.clock_out)) FROM attendance a WHERE a.user_id = :userId AND DATE(a.clock_in) BETWEEN :startDate AND :endDate", nativeQuery = true)
    Double getTotalWorkedHours(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	List<Attendance> findByUser_IdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

	boolean existsByUserAndDate(User user, LocalDate date);

//    @Query("SELECT SUM(TIMESTAMPDIFF(HOUR, a.clockIn, a.clockOut)) FROM Attendance a WHERE a.user.id = :userId")
//    long calculateTotalHoursWorked(Long userId);
}