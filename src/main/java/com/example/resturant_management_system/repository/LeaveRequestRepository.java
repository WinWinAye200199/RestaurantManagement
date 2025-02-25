package com.example.resturant_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.resturant_management_system.model.entities.LeaveRequest;
import com.example.resturant_management_system.security.UserPrincipal;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findAllByUser_Id(Long userId);
    List<LeaveRequest> findByUser(UserPrincipal user);
    List<LeaveRequest> findAll();
}
